package com.freelanceautomation.backend.cv.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class CvTextExtractor {

    private static final Charset WINDOWS_1252 = Charset.forName("Windows-1252");

    public String extractText(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String lowerName = originalFilename == null ? "" : originalFilename.toLowerCase();

        if (lowerName.endsWith(".pdf")) {
            return extractPdfText(file);
        }

        if (lowerName.endsWith(".docx")) {
            return extractDocxText(file);
        }

        throw new IllegalArgumentException("UNSUPPORTED_CV_FORMAT");
    }

    private String extractPdfText(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();

        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setAddMoreFormatting(true);
            stripper.setLineSeparator("\n");
            stripper.setWordSeparator(" ");

            String text = stripper.getText(document);
            return text == null ? "" : normalize(text);
        }
    }

    private String extractDocxText(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

            String text = extractor.getText();
            return text == null ? "" : normalize(text);
        }
    }

    private String normalize(String text) {
        String fixed = repairMojibakeDeep(text);

        return fixed
                .replace("\u00A0", " ")
                .replace("’", "'")
                .replace("`", "'")
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();
    }

    private String repairMojibakeDeep(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        String best = text;
        int bestScore = scoreText(best);

        String[] candidates = new String[] {
                text,
                decode(text, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8),
                decode(text, WINDOWS_1252, StandardCharsets.UTF_8),
                decode(decode(text, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8), WINDOWS_1252, StandardCharsets.UTF_8),
                decode(decode(text, WINDOWS_1252, StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8),
                decodeTwice(text, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8),
                decodeTwice(text, WINDOWS_1252, StandardCharsets.UTF_8)
        };

        for (String candidate : candidates) {
            int score = scoreText(candidate);
            if (score > bestScore) {
                best = candidate;
                bestScore = score;
            }
        }

        return best;
    }

    private String decode(String text, Charset wrongCharset, Charset targetCharset) {
        try {
            return new String(text.getBytes(wrongCharset), targetCharset);
        } catch (Exception e) {
            return text;
        }
    }

    private String decodeTwice(String text, Charset wrongCharset, Charset targetCharset) {
        return decode(decode(text, wrongCharset, targetCharset), wrongCharset, targetCharset);
    }

    private int scoreText(String text) {
        if (text == null || text.isBlank()) {
            return Integer.MIN_VALUE;
        }

        int score = 0;

        score += countOccurrences(text, "é") * 5;
        score += countOccurrences(text, "è") * 5;
        score += countOccurrences(text, "à") * 5;
        score += countOccurrences(text, "ù") * 5;
        score += countOccurrences(text, "ç") * 5;
        score += countOccurrences(text, "Étudiant") * 30;
        score += countOccurrences(text, "Développement") * 30;
        score += countOccurrences(text, "expérience") * 20;
        score += countOccurrences(text, "données") * 20;
        score += countOccurrences(text, "compétences") * 20;
        score += countOccurrences(text, "à") * 3;
        score += countOccurrences(text, "’") * 3;
        score += countOccurrences(text, "'") * 2;

        score -= countOccurrences(text, "Ã") * 20;
        score -= countOccurrences(text, "â") * 20;
        score -= countOccurrences(text, "Å") * 20;
        score -= countOccurrences(text, "�") * 40;
        score -= countOccurrences(text, "lâ") * 10;
        score -= countOccurrences(text, "DÃ") * 15;
        score -= countOccurrences(text, "Ãt") * 15;

        return score;
    }

    private int countOccurrences(String text, String token) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(token, index)) != -1) {
            count++;
            index += token.length();
        }

        return count;
    }
}
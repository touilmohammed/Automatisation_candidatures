package com.freelanceautomation.backend.cv.service;

import com.freelanceautomation.backend.cv.dto.CvResponse;
import com.freelanceautomation.backend.cv.dto.CvTextResponse;
import com.freelanceautomation.backend.cv.entity.CvDocument;
import com.freelanceautomation.backend.cv.parser.CvTextExtractor;
import com.freelanceautomation.backend.cv.repository.CvDocumentRepository;
import com.freelanceautomation.backend.user.entity.User;
import com.freelanceautomation.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

@Service
public class CvService {

    private final CvDocumentRepository cvRepository;
    private final UserRepository userRepository;
    private final CvTextExtractor cvTextExtractor;
    private final Path storageDirectory;

    public CvService(
            CvDocumentRepository cvRepository,
            UserRepository userRepository,
            CvTextExtractor cvTextExtractor,
            @Value("${app.storage.cv-directory}") String storageDir
    ) throws IOException {
        this.cvRepository = cvRepository;
        this.userRepository = userRepository;
        this.cvTextExtractor = cvTextExtractor;

        this.storageDirectory = Paths.get(storageDir).toAbsolutePath().normalize();
        Files.createDirectories(storageDirectory);
    }

    public CvResponse uploadCv(String email, MultipartFile file) throws IOException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("EMPTY_FILE");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("INVALID_FILENAME");
        }

        String lowerName = originalFilename.toLowerCase();
        if (!lowerName.endsWith(".pdf") && !lowerName.endsWith(".docx")) {
            throw new IllegalArgumentException("UNSUPPORTED_CV_FORMAT");
        }

        String extractedText = cvTextExtractor.extractText(file);
        extractedText = extractedText == null ? "" : extractedText.trim();

        if (lowerName.endsWith(".pdf") && extractedText.isBlank()) {
            throw new IllegalArgumentException("PDF_TEXT_EXTRACTION_FAILED");
        }

        System.out.println("Extracted text length = " + extractedText.length());
        System.out.println("Extracted text preview = " +
                extractedText.substring(0, Math.min(300, extractedText.length())));
        
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        Path target = storageDirectory.resolve(filename);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        CvDocument document = cvRepository.findByUserEmail(email)
                .orElse(CvDocument.builder().user(user).build());

        document.setOriginalFilename(originalFilename);
        document.setMimeType(file.getContentType());
        document.setStoragePath(target.toString());
        document.setExtractedText(extractedText);
        document.setUploadedAt(Instant.now());

        CvDocument saved = cvRepository.save(document);

        return new CvResponse(
                saved.getId(),
                saved.getOriginalFilename(),
                saved.getMimeType(),
                saved.getUploadedAt()
        );
    }

    public CvResponse getMyCv(String email) {
        CvDocument doc = cvRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("CV_NOT_FOUND"));

        return new CvResponse(
                doc.getId(),
                doc.getOriginalFilename(),
                doc.getMimeType(),
                doc.getUploadedAt()
        );
    }

    public CvTextResponse getMyCvText(String email) {
        CvDocument doc = cvRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("CV_NOT_FOUND"));

        String text = doc.getExtractedText();
        if (text == null) {
            text = "";
        }

        return new CvTextResponse(
                doc.getOriginalFilename(),
                text,
                text.length()
        );
    }
}
package com.freelanceautomation.backend.cv.controller;

import com.freelanceautomation.backend.cv.dto.CvResponse;
import com.freelanceautomation.backend.cv.dto.CvTextResponse;
import com.freelanceautomation.backend.cv.service.CvService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cv")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @PostMapping("/upload")
    public CvResponse uploadCv(@RequestParam("file") MultipartFile file,
                               Authentication authentication) throws Exception {

        return cvService.uploadCv(authentication.getName(), file);
    }

    @GetMapping("/me")
    public CvResponse getMyCv(Authentication authentication) {

        return cvService.getMyCv(authentication.getName());
    }

    @GetMapping("/me/text")
    public CvTextResponse getMyCvText(Authentication authentication) {
        return cvService.getMyCvText(authentication.getName());
    }
}
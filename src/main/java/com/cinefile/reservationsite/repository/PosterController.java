package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.service.PosterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/posters")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PosterController {
    private final PosterService posterService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPoster(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");

        String generatedFileName = posterService.uploadPoster(file);

        return ResponseEntity.status(HttpStatus.CREATED).body(generatedFileName);
    }


}

package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.PosterPost;
import com.cinefile.reservationsite.service.PosterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/posters")
@RequiredArgsConstructor
public class PosterController {
    private final PosterService posterService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadPoster(@RequestPart("file") MultipartFile file, Principal principal) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plik nie może być pusty!");
        }
        return posterService.uploadPoster(file, principal);
    }
}

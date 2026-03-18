package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.PosterPost;
import com.cinefile.reservationsite.model.Movie;
import com.cinefile.reservationsite.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PosterService {
    private final S3Client s3Client;
    private final MovieRepository movieRepository;
    @Value("${cloudflare.r2.bucket}")
    private String bucketName;
    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    public String uploadPoster(MultipartFile file, Principal principal) {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            if (principal != null) {
                log.info("File uploaded successfully by {}", principal.getName());
            }
            return publicUrl + "/" + fileName;

        } catch (Exception e) {
            log.error("Error while uploading file to S3: {}", e.getMessage(), e);
            throw new RuntimeException("Nie udało się wgrać pliku do chmury");
        }
    }
}

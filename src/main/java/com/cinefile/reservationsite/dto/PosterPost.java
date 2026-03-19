package com.cinefile.reservationsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PosterPost {
    private String title;
    private MultipartFile file;
}

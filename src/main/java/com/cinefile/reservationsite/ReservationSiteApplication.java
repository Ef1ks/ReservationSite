package com.cinefile.reservationsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReservationSiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationSiteApplication.class, args);
    }

}

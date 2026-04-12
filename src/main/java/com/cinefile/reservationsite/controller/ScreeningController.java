package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.ScreeningRes;
import com.cinefile.reservationsite.service.ScreeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin_panel")
@RequiredArgsConstructor
public class ScreeningController {
    final private ScreeningService screeningService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addScreening(@Valid @RequestBody ScreeningRes req) {
        screeningService.addScreening(req);
    }

    @GetMapping
    public List<ScreeningRes> getScreenings( ) {
        return screeningService.getScreenings();
    }

    @GetMapping("/btw")
    public List<ScreeningRes> getScreeningsBetween(@RequestParam LocalDateTime from,
                                                   @RequestParam LocalDateTime to){
        return screeningService.getScreeningsBetween(from,to);
    }

}

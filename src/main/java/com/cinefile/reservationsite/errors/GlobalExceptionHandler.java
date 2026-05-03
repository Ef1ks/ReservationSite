package com.cinefile.reservationsite.errors;

import com.cinefile.reservationsite.dto.ErrorResponse;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(RuntimeException e) {
        log.warn("Błąd biznesowy: {}", e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Błąd walidacji danych: {}", errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Naruszenie integralności bazy danych: {}", ex.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Operacja nie powiodła się: konflikt danych (np. taki rekord już istnieje)."
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // Zwraca 409 Conflict
    public ErrorResponse handleEntityExistsException(EntityExistsException ex) {

        // Logujemy to jako ostrzeżenie (warn), a nie błąd (error),
        // ponieważ to naturalne zachowanie użytkownika, a nie awaria serwera.
        log.warn("Konflikt danych (Duplikat): {}", ex.getMessage());

        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage() // np. "Email już zajęty."
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllUncaughtExceptions(Exception e) {
        log.error("Nieoczekiwany błąd serwera:", e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Wystąpił nieoczekiwany błąd serwera. Spróbuj ponownie później."
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("Nie znaleziono zasobu: {}", e.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialsException(Exception e) {
        log.error("Zalogowano pod złymi danymi logowania: {}", e.getMessage());
        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Podano błędne dane"
        );
    }

}

package br.com.blackbeard.blackbeardapi.exceptions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResourceExceptionHandler {

    public static final String VALIDATION_ERROR = "Validation error";
    public static final String ERROR_OF_ARCHIVE = "error of archive";
    public static final String STORAGE_ERROR = "Storage error";
    public static final String IMAGE_SIZE_EXCEEDED = "Image size exceeded";
    public static final String BARBER_SHOP_IMAGE_ERROR = "Barber shop image error";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError<Map<String, String>>> validation(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        var errors = new HashMap<String, String>();

        e.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(),
                fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : ""));

        return ResponseEntity.badRequest().body(new StandardError<>(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                VALIDATION_ERROR,
                Calendar.getInstance().getTimeInMillis(),
                request.getRequestURI()));
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardError<String>> file(
            FileException e, HttpServletRequest request) {
        var exception = new StandardError<>(
                HttpStatus.BAD_REQUEST.value(),
                ERROR_OF_ARCHIVE,
                e.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler({AmazonServiceException.class, AmazonClientException.class, AmazonS3Exception.class})
    public ResponseEntity<StandardError<String>> amazonService(
            AmazonClientException e, HttpServletRequest request) {
        var exception =
                new StandardError<>(
                        HttpStatus.BAD_REQUEST.value(),
                        STORAGE_ERROR,
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<StandardError<String>> maxUploadSizeExceededExceptionHandler(
            MaxUploadSizeExceededException e, HttpServletRequest request) {
        var exception =
                new StandardError<>(
                        HttpStatus.BAD_REQUEST.value(),
                        IMAGE_SIZE_EXCEEDED,
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception);
    }

    @ExceptionHandler(BarberShopImageLimitException.class)
    public ResponseEntity<StandardError<String>> barberShopImageLimitExceptionHandler(
            BarberShopImageLimitException e, HttpServletRequest request) {
        var exception =
                new StandardError<>(
                        HttpStatus.BAD_REQUEST.value(),
                        BARBER_SHOP_IMAGE_ERROR,
                        e.getMessage(),
                        System.currentTimeMillis(),
                        request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception);
    }

}
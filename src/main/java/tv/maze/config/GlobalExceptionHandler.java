package tv.maze.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.servlet.http.HttpServletRequest;
import tv.maze.util.ErrorComun;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorComun> handleHttpClientError(HttpClientErrorException ex, HttpServletRequest request) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        String message = status == HttpStatus.NOT_FOUND ? "El show no fue encontrado" : ex.getStatusText();
        
        ErrorComun error = new ErrorComun(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }


    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorComun> handleHttpServerError(HttpServerErrorException ex, HttpServletRequest request) {
        ErrorComun error = new ErrorComun(
                LocalDateTime.now(),
                HttpStatus.BAD_GATEWAY.value(), 
                HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                "Error en el servicio",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorComun> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorComun error = new ErrorComun(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Error en el servidor",
                request.getRequestURI()
        );
        System.err.println("Error: "+ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}


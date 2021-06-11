package com.example.demo.desafiospring.exception;

import com.example.demo.desafiospring.dto.response.ErrorResponseDTO;
import com.example.demo.desafiospring.util.Time;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    private ErrorResponseDTO errorResponseDTO;

    //Manejamos las posibles Excepciones

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> exception(Exception e){
        errorResponseDTO = new ErrorResponseDTO("[Exception] - " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(), Time.getTime());
        return new ResponseEntity<>(errorResponseDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> exception(RuntimeException e){
        errorResponseDTO = new ErrorResponseDTO("[RuntimeException] - " + e.getMessage(),
                HttpStatus.NOT_FOUND.value(), Time.getTime());
        return new ResponseEntity<>(errorResponseDTO,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponseDTO> exception(NullPointerException e){
        errorResponseDTO = new ErrorResponseDTO("[NullPointerException] - " + e.getMessage(),
                HttpStatus.NOT_FOUND.value(), Time.getTime());
        return new ResponseEntity<>(errorResponseDTO,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponseDTO> exception(NumberFormatException e){
        errorResponseDTO = new ErrorResponseDTO("[NumberFormatException] - " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(), Time.getTime());
        return new ResponseEntity<>(errorResponseDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

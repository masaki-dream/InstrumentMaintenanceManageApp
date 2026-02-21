package com.masaki.instrumentmaintenancemanageapp.controller;


import com.masaki.instrumentmaintenancemanageapp.exception.BadRequestException;
import com.masaki.instrumentmaintenancemanageapp.exception.InstrumentNotFoundException;
import com.masaki.instrumentmaintenancemanageapp.exception.InvalidMaintenanceStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    // 404
    @ExceptionHandler(InstrumentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(InstrumentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // 409
    @ExceptionHandler(InvalidMaintenanceStateException.class)
    public ResponseEntity<String> handleInvalid(InvalidMaintenanceStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // それ以外
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOther(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("想定外エラー");
    }
}

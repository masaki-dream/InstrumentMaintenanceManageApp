package com.masaki.instrumentmaintenancemanageapp.exception;

public class BadRequestException extends BusinessException {

    // 業務的に存在しない場合
    public BadRequestException(String message) {
        super(message);
    }
}

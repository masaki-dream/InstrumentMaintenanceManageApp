package com.masaki.instrumentmaintenancemanageapp.exception;

public class InstrumentNotFoundException extends BusinessException {

    // 業務的に存在しない場合
    public InstrumentNotFoundException(String message) {
        super(message);
    }
}

package com.masaki.instrumentmaintenancemanageapp.exception;

public class InvalidMaintenanceStateException extends BusinessException {

    // 業務ルール違反の場合
    public InvalidMaintenanceStateException (String message){
        super(message);
    }

}

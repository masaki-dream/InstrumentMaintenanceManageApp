package com.masaki.instrumentmaintenancemanageapp.exception;

// 業務例外の親で、以下の内容をチェック
// ①メンテナンス中じゃないのに完了
// ②すでにメンテナンス中なのに開始
// ③権限がないのに操作
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}


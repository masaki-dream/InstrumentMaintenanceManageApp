package com.masaki.instrumentmaintenancemanageapp.controller.dto;

import java.time.LocalDateTime;

public class MaintenanceRequest {

    // メンテナンス内容（例：弦交換、点検）
    private String type;

    // ユーザーID
    private Long userId;

    // 実施日
    private LocalDateTime performedAt;

    // ===== setter =====
    public void setType(String type) { this.type = type; }
    public void setPerformedAt(LocalDateTime performedAt) { this.performedAt = performedAt; }

    // ===== getter =====
    public String getType() {
        return type;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }



}

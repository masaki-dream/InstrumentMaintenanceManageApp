package com.masaki.instrumentmaintenancemanageapp.controller.dto;

import java.time.LocalDateTime;

public class MaintenanceRequest {

    // メンテナンス内容（例：弦交換、点検）
    private String type;

    // ユーザーID
    private Long userId;

    // 実施日
    private LocalDateTime performedAt;

    // ===== getter =====
    public String getType() {
        return type;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public Long getUserId() {
        return userId;
    }


}

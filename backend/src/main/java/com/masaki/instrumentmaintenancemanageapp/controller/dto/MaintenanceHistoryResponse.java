package com.masaki.instrumentmaintenancemanageapp.controller.dto;

import java.time.LocalDateTime;

// メンテナンス履歴表示用DTO
public class MaintenanceHistoryResponse {

    private LocalDateTime performedAt;
    private String type;

    public MaintenanceHistoryResponse(
            LocalDateTime performedAt,
            String type
    ) {
        this.performedAt = performedAt;
        this.type = type;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public String getType() {
        return type;
    }


}

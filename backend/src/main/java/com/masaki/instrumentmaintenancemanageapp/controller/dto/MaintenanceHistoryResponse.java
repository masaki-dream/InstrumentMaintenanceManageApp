package com.masaki.instrumentmaintenancemanageapp.controller.dto;

import java.time.LocalDateTime;

// メンテナンス履歴表示用DTO
public class MaintenanceHistoryResponse {

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public MaintenanceHistoryResponse(
            LocalDateTime startedAt,
            LocalDateTime completedAt
    ) {
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }


}

package com.masaki.instrumentmaintenancemanageapp.controller.dto;

import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceStatus;

// 機材一覧取得用レスポンスDTO
public class InstrumentListResponse {
    private Long id;
    private String name;
    private MaintenanceStatus status;

    public InstrumentListResponse(Long id, String name, MaintenanceStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }
}

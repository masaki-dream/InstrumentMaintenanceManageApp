package com.masaki.instrumentmaintenancemanageapp.controller.dto;

import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceStatus;

// 機材一覧取得用レスポンスDTO
public class InstrumentListResponse {
    private Long id;
    private String name;
    private MaintenanceStatus status;
    private String maintenanceType;
    private String description;

    public InstrumentListResponse(Long id, String name, MaintenanceStatus status, String maintenanceType, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.maintenanceType = maintenanceType;
        this.description = description;
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

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public String getDescription() {
        return description;
    }
}

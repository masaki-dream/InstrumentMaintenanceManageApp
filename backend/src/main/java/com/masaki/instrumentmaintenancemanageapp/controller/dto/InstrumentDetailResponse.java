package com.masaki.instrumentmaintenancemanageapp.controller.dto;


import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceStatus;

import java.util.List;

// 機材詳細取得用レスポンスDTO
public class InstrumentDetailResponse {

    private Long id;
    private String name;
    private MaintenanceStatus status;
    private List<MaintenanceHistoryResponse> histories;

    public InstrumentDetailResponse(
            Long id,
            String name,
            MaintenanceStatus status,
            List<MaintenanceHistoryResponse> histories
    ) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.histories = histories;
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

    public List<MaintenanceHistoryResponse> getHistories() {
        return histories;
    }
}

package com.masaki.instrumentmaintenancemanageapp.controller;

import com.masaki.instrumentmaintenancemanageapp.controller.dto.InstrumentDetailResponse;
import com.masaki.instrumentmaintenancemanageapp.controller.dto.InstrumentListResponse;
import com.masaki.instrumentmaintenancemanageapp.controller.dto.MaintenanceRequest;
import com.masaki.instrumentmaintenancemanageapp.exception.BadRequestException;
import com.masaki.instrumentmaintenancemanageapp.service.MaintenanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    // Controller が使う Service
    private final MaintenanceService maintenanceService;

    public InstrumentController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // 機材一覧取得 API
//    @GetMapping
//    public List<InstrumentListResponse> getAll() {
//        return maintenanceService.getAll();
//    }
    // 機材一覧取得 API
    @GetMapping
    public List<InstrumentListResponse> getById() {
        return maintenanceService.getById();
    }

    // 機材詳細取得 API
    @GetMapping("/{id}")
    public InstrumentDetailResponse getInstrument(
            @PathVariable Long id) {

        // 取得したID（1件）の詳細表示
        return maintenanceService.getAll(id);
    }

    // メンテナンス開始 API
    @PostMapping("/{id}/maintenance/start")
    public ResponseEntity<Void> startMaintenance(
            @PathVariable Long id
    ) {

        // 入力チェック（400対象）
        if (id == null || id <= 0) {
            throw new BadRequestException("機材IDが不正です");
        }

        maintenanceService.startMaintenance(id);

        // 正常なので204
        return ResponseEntity.noContent().build();

    }

    // メンテナンス完了 API
    @PostMapping("/{id}/maintenance/complete")
    public ResponseEntity<Void> completeMaintenance(
            @PathVariable Long id
    ) {

        maintenanceService.completeMaintenance(id);

        // 正常なので204
        return ResponseEntity.noContent().build();
    }

    // メンテナンス登録API
    @PostMapping("/{id}/maintenances")
    public ResponseEntity<Void> registerMaintenance(
            @PathVariable Long id,
            @RequestBody MaintenanceRequest request
    ) {
        // メンテナンス登録
        maintenanceService.register(id, request);

        // 作成系なので 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}

package com.masaki.instrumentmaintenancemanageapp.controller;

import com.masaki.instrumentmaintenancemanageapp.controller.dto.*;
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

    // メンテナンス登録API
    @PostMapping("/{id}/maintenances/start")
    public ResponseEntity<Void> startWithHistory(@PathVariable Long id,
                                                 @RequestBody MaintenanceRequest request) {
        maintenanceService.startWithHistory(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // メンテナンス完了 API
    @PostMapping("/{id}/maintenances/complete")
    public ResponseEntity<Void> completeWithHistory(@PathVariable Long id,
                                                    @RequestBody MaintenanceRequest request) {
        maintenanceService.completeWithHistory(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 楽器新規作成API
    @PostMapping
    public ResponseEntity<Void> createInstrument(@RequestBody InstrumentCreateRequest request) {

        if (request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("nameが不正です");
        }

        Long createdId = maintenanceService.createInstrument(request);

        // 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 更新処理
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateInstrument(
            @PathVariable Long id,
            @RequestBody InstrumentUpdateRequest request
    ) {
        if (id == null || id <= 0) {
            throw new BadRequestException("機材IDが不正です");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("楽器名が未入力です");
        }

        maintenanceService.updateInstrument(id, request);
        return ResponseEntity.noContent().build(); // 204
    }

    // 楽器削除API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new BadRequestException("機材IDが不正です");
        }

        maintenanceService.deleteInstrument(id);

        return ResponseEntity.noContent().build(); // 204
    }

}

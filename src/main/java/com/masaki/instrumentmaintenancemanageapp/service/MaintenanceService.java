package com.masaki.instrumentmaintenancemanageapp.service;

import com.masaki.instrumentmaintenancemanageapp.controller.dto.InstrumentDetailResponse;
import com.masaki.instrumentmaintenancemanageapp.controller.dto.InstrumentListResponse;
import com.masaki.instrumentmaintenancemanageapp.controller.dto.MaintenanceHistoryResponse;
import com.masaki.instrumentmaintenancemanageapp.controller.dto.MaintenanceRequest;
import com.masaki.instrumentmaintenancemanageapp.domain.Instrument;
import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceStatus;
import com.masaki.instrumentmaintenancemanageapp.exception.InstrumentNotFoundException;
import com.masaki.instrumentmaintenancemanageapp.infrastructure.InstrumentEntity;
import com.masaki.instrumentmaintenancemanageapp.infrastructure.MaintenanceEntity;
import com.masaki.instrumentmaintenancemanageapp.repository.InstrumentEntityRepository;
//import com.masaki.instrumentmaintenancemanageapp.repository.InstrumentRepository;
import com.masaki.instrumentmaintenancemanageapp.repository.MaintenanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceService {

    // repositoryを宣言
//    private final InstrumentRepository instrumentRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final InstrumentEntityRepository instrumentEntityRepository;

    // コンストラクタ
    public MaintenanceService(MaintenanceRepository maintenanceRepository, InstrumentEntityRepository instrumentEntityRepository) {
//        this.instrumentRepository = repository;
        this.maintenanceRepository = maintenanceRepository;
        this.instrumentEntityRepository = instrumentEntityRepository;
    }

    // 機材一覧取得
    public List<InstrumentListResponse> getById() {

        // RepositoryからDomainを取得
        List<InstrumentEntity> instruments = instrumentEntityRepository.findAll();

        // Domain → DTO に変換
        return instruments.stream()
                .map(instrument ->
                        new InstrumentListResponse(
                                instrument.getId(),
                                instrument.getName(),
                                instrument.getStatus()
                        )
                )
                .toList();
    }

    // 機材詳細取得
//    public InstrumentDetailResponse getAll(Long id) {
//
//        // Repositoryから取得
//        Instrument instrument = instrumentRepository
//                .findById(id)
//                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));
//
//        // Domainの履歴をDTOに変換
//        List<MaintenanceHistoryResponse> histories =
//                instrument.getHistories().stream()
//                        .map(history ->
//                                new MaintenanceHistoryResponse(
//                                        history.getStartedAt(),
//                                        history.getCompletedAt()
//                                )
//                        )
//                        .toList();
//
//        // Domain → DTO に変換
//        return new InstrumentDetailResponse(
//                instrument.getId(),
//                instrument.getName(),
//                instrument.getStatus(),
//                histories
//        );
//    }

    // 機材詳細取得
    public InstrumentDetailResponse getAll(Long id) {

        InstrumentEntity  instrument = instrumentEntityRepository
                .findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));

        // MaintenanceRepositoryから履歴取得
        List<MaintenanceHistoryResponse> histories =
                maintenanceRepository.findByInstrumentId(id).stream()
                        .map(maintenance ->
                                new MaintenanceHistoryResponse(
                                        maintenance.getPerformedAt(),
                                        null   // 完了日時あるならここに
                                )
                        )
                        .toList();

        return new InstrumentDetailResponse(
                instrument.getId(),
                instrument.getName(),
                instrument.getStatus(),
                histories
        );
    }

    // このメソッド内のDB処理を1つのトランザクションとして扱う
    @Transactional
    public void register(Long instrumentId, MaintenanceRequest request) {

        // 存在チェック（業務例外）
//        if (!instrumentRepository.existsById(instrumentId)) {
//            throw new InstrumentNotFoundException("機材が存在しません");
//        }

        // ① InstrumentEntity を取得（存在チェック込み）
        InstrumentEntity instrumentEntity = instrumentEntityRepository
                .findById(instrumentId)
                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));

        // メンテナンスエンティティを新しく生成
        MaintenanceEntity maintenance = new MaintenanceEntity();

        maintenance.setInstrument(instrumentEntity);

        maintenance.setUserId(request.getUserId());
        maintenance.setType(request.getType());
        maintenance.setPerformedAt(request.getPerformedAt());

        // メンテナンス情報をDBに保存
        maintenanceRepository.save(maintenance);
    }



// ---------------------------- 例外処理 Start --------------------------------------------

    // メンテナンス開始処理
    public void startMaintenance(Long instrumentId) {


        // Repositoryから取得
        InstrumentEntity instrument = instrumentEntityRepository
                .findById(instrumentId)
                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));

        // 業務ルール実行（Domainに委譲）される、既にメンテナンス中なら例外処理が飛ぶ
        instrument.startMaintenance();

        // 状態変更後の機材を保存
        instrumentEntityRepository.save(instrument);
    }


    // メンテナンス終了処理
    public void completeMaintenance(Long instrumentId) {

        // Repositoryから取得
        InstrumentEntity instrument = instrumentEntityRepository
                .findById(instrumentId)
                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));


        // 業務ルール実行（Domainに委譲）される、既にメンテナンス中なら例外処理が飛ぶ
        instrument.completeMaintenance();

        // 状態変更後の機材を保存
        instrumentEntityRepository.save(instrument);
    }


    // ---------------------------- 例外処理 End --------------------------------------------

}

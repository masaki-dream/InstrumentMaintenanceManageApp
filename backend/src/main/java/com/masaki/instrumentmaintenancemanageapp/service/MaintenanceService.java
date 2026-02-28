package com.masaki.instrumentmaintenancemanageapp.service;

import com.masaki.instrumentmaintenancemanageapp.controller.dto.*;
import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceAction;
import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceStatus;
import com.masaki.instrumentmaintenancemanageapp.exception.BadRequestException;
import com.masaki.instrumentmaintenancemanageapp.exception.InstrumentNotFoundException;
import com.masaki.instrumentmaintenancemanageapp.infrastructure.InstrumentEntity;
import com.masaki.instrumentmaintenancemanageapp.infrastructure.MaintenanceEntity;
import com.masaki.instrumentmaintenancemanageapp.infrastructure.UserEntity;
import com.masaki.instrumentmaintenancemanageapp.repository.InstrumentEntityRepository;
import com.masaki.instrumentmaintenancemanageapp.repository.MaintenanceRepository;
import com.masaki.instrumentmaintenancemanageapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceService {

    // repositoryを宣言
    private final MaintenanceRepository maintenanceRepository;
    private final InstrumentEntityRepository instrumentEntityRepository;
    private final UserRepository userRepository;

    // コンストラクタ
    public MaintenanceService(MaintenanceRepository maintenanceRepository, InstrumentEntityRepository instrumentEntityRepository, UserRepository userRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.instrumentEntityRepository = instrumentEntityRepository;
        this.userRepository = userRepository;
    }

    // 更新API
    @Transactional
    public void updateInstrument(Long id, InstrumentUpdateRequest request) {

        InstrumentEntity instrument = instrumentEntityRepository.findById(id)
                .orElseThrow(() -> new InstrumentNotFoundException("機材が見つかりません"));

        // レスポンスがNULLでも空でもない場合、名前を変更
        if (request.getName() != null && !request.getName().isBlank()) {
            instrument.setName(request.getName());
        }

        // 任意項目はそのまま上書き（nullなら触らない）
        if (request.getMaintenanceType() != null) {
            instrument.setMaintenanceType(request.getMaintenanceType());
        }

        // 説明欄がNULLではない場合、説明欄を変更
        if (request.getDescription() != null) {
            instrument.setDescription(request.getDescription());
        }
        // save()は不要（JPAのdirty checkingで更新される）
    }

    // 削除API
    public void deleteInstrument(Long id) {

        InstrumentEntity instrument =
                instrumentEntityRepository.findById(id)
                        .orElseThrow(() -> new InstrumentNotFoundException("機材が見つかりません"));

        instrumentEntityRepository.delete(instrument);
    }

    // 機材一覧取得（ログインしたユーザの機材一覧を表示する。）
    public List<InstrumentListResponse> getById() {

        // ① JWTからusername取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // ② username → UserEntity → userId取得
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        // ③ userIdで絞り込み
        return instrumentEntityRepository.findByUserId(user.getId())
                .stream()
                .map(instrument -> new InstrumentListResponse(
                        instrument.getId(),
                        instrument.getName(),
                        instrument.getStatus(),
                        instrument.getMaintenanceType(),
                        instrument.getDescription()
                ))
                .toList();
    }

    // 機材詳細取得（所有者チェック付き）
    public InstrumentDetailResponse getAll(Long id) {

        // ① JWTからusername取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // ② username → UserEntity → userId取得
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        // ③ id + userId で取得（他人の機材は取れない）
        InstrumentEntity instrument = instrumentEntityRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));

        // ④ 履歴取得（※できれば userId も条件に入れるのが安全）
        List<MaintenanceHistoryResponse> histories =
                maintenanceRepository.findByInstrumentId(id).stream()
                        .map(m -> new MaintenanceHistoryResponse(
                                m.getPerformedAt(),
                                m.getType()
                        ))
                        .toList();

        return new InstrumentDetailResponse(
                instrument.getId(),
                instrument.getName(),
                instrument.getStatus(),
                histories
        );
    }

    // 楽器新規作成処理
    @Transactional
    public Long createInstrument(InstrumentCreateRequest request) {

        // JWT認証済みユーザー名を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // username から UserEntity を引いて userId を確定
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InstrumentNotFoundException("ユーザーが見つかりません: " + username));

        // Instrument作成
        InstrumentEntity entity = new InstrumentEntity();
        entity.setUserId(user.getId()); // userIdはjwtで引っ張ってくる
        entity.setName(request.getName());
        entity.setMaintenanceType(request.getMaintenanceType());
        entity.setDescription(request.getDescription());

        // 初期状態は固定
        entity.setStatus(MaintenanceStatus.NOT_MAINTAINED);

        InstrumentEntity saved = instrumentEntityRepository.save(entity);
        return saved.getId();
    }

    // 詳細画面からのメンテ開始処理（履歴追加も行う）
    @Transactional
    public void startWithHistory(Long instrumentId, MaintenanceRequest request) {

        // ===== バリデーション =====
        validateMaintenanceRequest(request);

        // ===== JWTユーザー特定 =====
        Long userId = getCurrentUserId();

        // ===== 自分の機材だけ取得 =====
        InstrumentEntity instrument = instrumentEntityRepository
                .findByIdAndUserId(instrumentId, userId)
                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));

        // ===== 状態遷移（再開OK仕様なら COMPLETE でも startMaintenance() できるようにする） =====
        instrument.startMaintenance();

        // ===== 履歴追加 =====
        MaintenanceEntity history = new MaintenanceEntity();
        history.setInstrument(instrument);
        history.setUserId(userId);
        history.setType(request.getType());
        history.setPerformedAt(request.getPerformedAt());
        history.setAction(MaintenanceAction.START);

        maintenanceRepository.save(history);
        // instrument は dirty checking で更新される（念のため save してもOK）
    }

    // 詳細画面からのメンテ終了処理（履歴追加も行う）
    @Transactional
    public void completeWithHistory(Long instrumentId, MaintenanceRequest request) {

        // ===== バリデーション =====
        validateMaintenanceRequest(request);

        // ===== JWTユーザー特定 =====
        Long userId = getCurrentUserId();

        // ===== 自分の機材だけ取得 =====
        InstrumentEntity instrument = instrumentEntityRepository
                .findByIdAndUserId(instrumentId, userId)
                .orElseThrow(() -> new InstrumentNotFoundException("機材が存在しません"));

        // ===== 状態遷移 =====
        instrument.completeMaintenance();

        // ===== 履歴追加 =====
        MaintenanceEntity history = new MaintenanceEntity();
        history.setInstrument(instrument);
        history.setUserId(userId);
        history.setType(request.getType());
        history.setPerformedAt(request.getPerformedAt());
        history.setAction(MaintenanceAction.COMPLETE);

        maintenanceRepository.save(history);
    }

    // 現在ログインしているユーザーID取得
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + username));

        return user.getId();
    }

    // バリデーション（履歴登録の際に、不正な入力を防ぐ処理）
    private void validateMaintenanceRequest(MaintenanceRequest request) {
        if (request.getType() == null || request.getType().isBlank()) {
            throw new BadRequestException("メンテ内容(type)は必須です");
        }
        if (request.getPerformedAt() == null) {
            throw new BadRequestException("実施日(performedAt)は必須です");
        }

        LocalDateTime min = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime max = LocalDateTime.now().plusMinutes(10);

        if (request.getPerformedAt().isBefore(min) || request.getPerformedAt().isAfter(max)) {
            throw new BadRequestException("実施日は2000年〜現在（+10分）までで入力してください");
        }
    }
}

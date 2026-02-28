package com.masaki.instrumentmaintenancemanageapp.infrastructure;

import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceAction;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 機材に対して実施されたメンテナンス履歴を管理するエンティティ
 * どの機材に、誰が、いつ、どのような作業を行ったかを保持する
 */
@Entity
@Table(name = "maintenance")
public class MaintenanceEntity {

    // ===== 主キー =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // DBにID採番を任せる
    private Long maintenanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private InstrumentEntity instrument;

    // ===== 所有者 =====
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // ===== メンテナンス作業の種別 =====
    // 例：弦交換、クリーニング、調整、修理など
    private String type;

    // ===== メンテナンス作業を実施した日時 =====
    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    // ===== メンテナンス実施時の状態 =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceAction action;

    // ===== setter =====
    public void setInstrument(InstrumentEntity instrument) {
        this.instrument = instrument;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public void setAction(MaintenanceAction action) { this.action = action; }

    // ===== getter =====
    public InstrumentEntity getInstrument() {
        return instrument;
    }

    public Long getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public MaintenanceAction getAction() { return action; }
}

package com.masaki.instrumentmaintenancemanageapp.infrastructure;

import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceStatus;
import com.masaki.instrumentmaintenancemanageapp.exception.InvalidMaintenanceStateException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


// 機材テーブル用 Entityで、DBとの対応だけを責務とする
@Entity
@Table(name = "instruments")
public class InstrumentEntity {

    // ===== 主キー =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // DBにID採番を任せる
    @Column(name = "instrument_id")
    private Long id;

    // ===== この機材に紐づくメンテナンス履歴一覧 =====
    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL)
    private List<MaintenanceEntity> maintenances;

    // ===== 所有者 =====
    @Column(name = "UserID", nullable = false)
    // nullable=false はDB的にも必須
    private Long userId;

    // ===== 機材名 =====
    @Column(nullable = false)
    private String name;

    // ===== メンテナンスタイプ =====
    @Column(name = "maintenance_type")
    private String maintenanceType;

    // ===== 状態 =====
    @Enumerated(EnumType.STRING)
    // enum名を文字列で保存（NOT_MAINTAINED, MAINTAINING, COMPLETED）
    @Column(nullable = false)
    private MaintenanceStatus status;

    // ===== 説明 =====
    private String description;

    // ===== 日付系 =====
    @Column(name = "maintenance_date")
    private LocalDateTime maintenanceDate;

    @Column(name = "next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ===== 作成日時自動設定 =====
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ===== JPA用（必須） =====
    public InstrumentEntity() {
        // JPAはリフレクションで使う
    }

    // ===== setter =====
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaintenanceDate(LocalDateTime maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDateTime nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    // ===== getter =====
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public String getMaintenanceType() { return maintenanceType; }

    public String getDescription() { return description; }

    // ---------------------------- 例外処理 Start --------------------------------------------
    // メンテナンス開始
    public void startMaintenance() {

        if (this.status == MaintenanceStatus.MAINTAINING) {
            throw new InvalidMaintenanceStateException("既にメンテナンスされています。");
        }
        this.status = MaintenanceStatus.MAINTAINING;
    }

    // メンテナンス終了
    public void completeMaintenance() {

        if (this.status != MaintenanceStatus.MAINTAINING) {
            throw new InvalidMaintenanceStateException("メンテナンス中でない機材は完了できません");
        }

        this.status = MaintenanceStatus.COMPLETED;
    }

    // ---------------------------- 例外処理 End --------------------------------------------

}

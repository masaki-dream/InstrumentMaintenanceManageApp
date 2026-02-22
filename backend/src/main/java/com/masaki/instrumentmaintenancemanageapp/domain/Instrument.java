package com.masaki.instrumentmaintenancemanageapp.domain;

import com.masaki.instrumentmaintenancemanageapp.exception.InvalidMaintenanceStateException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Instrument {

    private Long id;

    private String name;

    private MaintenanceStatus status;

    // メンテナンス履歴一覧
//    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MaintenanceHistory> histories = new ArrayList<>();

    // ★ JPA用デフォルトコンストラクタ（必須）
    protected Instrument() {
    }

    public Instrument(String name, MaintenanceStatus status) {
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public String getName(){
        return name;
    }

    // ---------------------------- 例外処理 Start --------------------------------------------
    // メンテナンス開始
    public void startMaintenance() {

        if (this.status == MaintenanceStatus.MAINTAINING) {
            throw new InvalidMaintenanceStateException("既にメンテナンスされています。");
        }
        this.status = MaintenanceStatus.MAINTAINING;

        // 履歴をドメイン責務として生成
//        MaintenanceHistory history =
//                MaintenanceHistory.start(this);
//
//        histories.add(history);
//
//        // 「どの履歴が作られたか」を外から分かるようにするため
//        return history;
    }

    // メンテナンス終了
    public void completeMaintenance() {

        if (this.status != MaintenanceStatus.MAINTAINING) {
            throw new InvalidMaintenanceStateException("メンテナンス中でない機材は完了できません");
        }

        // 最新の履歴を完了させる
//        MaintenanceHistory latest =
//                histories.get(histories.size() - 1);
//
//        latest.completeMaintenance();

        this.status = MaintenanceStatus.COMPLETED;
    }

    // GET APIのための出口
//    public List<MaintenanceHistory> getHistories() {
//        return histories;
//    }


    // ---------------------------- 例外処理 End --------------------------------------------
}

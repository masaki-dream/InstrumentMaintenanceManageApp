//package com.masaki.instrumentmaintenancemanageapp.domain;
//
//import com.masaki.instrumentmaintenancemanageapp.exception.InvalidMaintenanceStateException;
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//
//// メンテナンス履歴
//@Entity
//@Table(name = "maintenance_histories")
//public class MaintenanceHistory {
//
//    // この履歴そのもののID
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // どの機材の履歴かを示すID（ギターだったら1とか）
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "instrument_id", nullable = false)
//    private Instrument instrument;
//
////    private Long instrumentId;
//
//
//    private LocalDateTime startedAt;
//
//    private LocalDateTime completedAt;
//
//    // ★ JPA用デフォルトコンストラクタ（必須）
//    protected MaintenanceHistory() {
//    }
//
////    public MaintenanceHistory(Long id, Long instrumentId, LocalDateTime startedAt) {
////        this.id = id;
////        this.instrumentId = instrumentId;
////        this.startedAt = startedAt;
////        // 開始時点では未完了
////        this.completedAt = null;
////    }
//    private MaintenanceHistory(Instrument instrument, LocalDateTime startedAt) {
//        this.instrument = instrument;
//        this.startedAt = startedAt;
//        this.completedAt = null;
//    }
//
//    // メンテナンス開始
////    public static MaintenanceHistory start(Long instrumentId) {
////        return new MaintenanceHistory(
////                null,
////                instrumentId,
////                LocalDateTime.now()
////        );
////    }
//
//    // メンテナンス開始（ファクトリメソッド）
//    public static MaintenanceHistory start(Instrument instrument) {
//        return new MaintenanceHistory(
//                instrument,
//                LocalDateTime.now()
//        );
//    }
//
//    // メンテナンス完了
//    public void completeMaintenance() {
//
//        // すでに完了していたら業務的におかしい
//        if (this.completedAt != null) {
//            throw new InvalidMaintenanceStateException("既に完了しています");
//        }
//
//        // 今の日付を履歴完了日に格納
//        this.completedAt = LocalDateTime.now();
//    }
//
//    // ===== getter =====
//    public Long getId() {
//        return id;
//    }
//
//    public Instrument getInstrument() {
//        return instrument;
//    }
//
//    public LocalDateTime getStartedAt() {
//        return startedAt;
//    }
//
//    public LocalDateTime getCompletedAt() {
//        return completedAt;
//    }
//}
//

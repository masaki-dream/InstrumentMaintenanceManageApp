package com.masaki.instrumentmaintenancemanageapp.repository;

import com.masaki.instrumentmaintenancemanageapp.domain.MaintenanceAction;
import com.masaki.instrumentmaintenancemanageapp.infrastructure.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaintenanceRepository
        extends JpaRepository<MaintenanceEntity, Long> {

    List<MaintenanceEntity> findByInstrumentId(Long instrumentId);

    // 直近のメンテ開始を取る（instrumentId + action + performedAt降順で先頭）
    Optional<MaintenanceEntity> findTopByInstrumentIdAndActionOrderByPerformedAtDesc(
            Long instrumentId,
            MaintenanceAction action
    );
}

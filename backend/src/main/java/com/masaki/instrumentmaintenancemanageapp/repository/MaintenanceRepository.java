package com.masaki.instrumentmaintenancemanageapp.repository;

import com.masaki.instrumentmaintenancemanageapp.infrastructure.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository
        extends JpaRepository<MaintenanceEntity, Long> {

    List<MaintenanceEntity> findByInstrumentId(Long instrumentId);

}
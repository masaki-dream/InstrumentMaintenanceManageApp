package com.masaki.instrumentmaintenancemanageapp.repository;

import com.masaki.instrumentmaintenancemanageapp.infrastructure.InstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentEntityRepository
        extends JpaRepository<InstrumentEntity, Long> {
}
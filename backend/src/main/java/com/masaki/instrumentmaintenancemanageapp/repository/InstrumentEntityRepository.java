package com.masaki.instrumentmaintenancemanageapp.repository;

import com.masaki.instrumentmaintenancemanageapp.infrastructure.InstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstrumentEntityRepository
        extends JpaRepository<InstrumentEntity, Long> {

    List<InstrumentEntity> findByUserId(Long userId);

    Optional<InstrumentEntity> findByIdAndUserId(Long id, Long userId);
}
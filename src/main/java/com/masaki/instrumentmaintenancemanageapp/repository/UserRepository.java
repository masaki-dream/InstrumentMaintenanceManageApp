package com.masaki.instrumentmaintenancemanageapp.repository;

import com.masaki.instrumentmaintenancemanageapp.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}

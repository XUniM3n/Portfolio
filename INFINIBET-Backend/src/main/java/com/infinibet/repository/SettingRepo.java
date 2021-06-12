package com.infinibet.repository;

import com.infinibet.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepo extends JpaRepository<Setting, Long> {
    Optional<Setting> findByKey(String key);
}

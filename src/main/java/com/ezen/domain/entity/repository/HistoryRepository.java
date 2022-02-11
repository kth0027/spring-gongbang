package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {
}

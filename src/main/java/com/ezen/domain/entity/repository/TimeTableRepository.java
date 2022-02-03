package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.TimeTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTableEntity, Integer> {
}

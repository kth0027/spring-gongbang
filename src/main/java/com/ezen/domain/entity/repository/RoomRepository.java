package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {


    Page<RoomEntity> findAll(Pageable pageable);
}

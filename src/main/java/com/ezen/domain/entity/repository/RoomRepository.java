package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    // 1. 지역만을 선택했을 때 쿼리문
    @Query(nativeQuery = true, value = "select * from room where roomLocal = :local")
    List<RoomEntity> findRoomByLocal(@Param("local") String local);

    // 2. 카테고리만을 선택했을 때 쿼리문
    @Query(nativeQuery = true, value = "select * from room where roomCategory = :category")
    List<RoomEntity> findRoomByCategory(@Param("category") String category);

    // 3. 지역, 카테고리 동시 선택 시 쿼리문
    @Query(nativeQuery = true, value = "select * from room where roomLocal = :local and roomCategory = :category")
    List<RoomEntity> findRoomByLocalAndCategory(@Param("local") String local, @Param("category") String category);

}

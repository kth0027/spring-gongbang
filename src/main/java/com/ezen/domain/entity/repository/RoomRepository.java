package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    // 1. 지역만을 선택 했을 때 쿼리문
    @Query( nativeQuery = true , value = "select * from room where roomLocal = :local")
    List<RoomEntity> findRoomByLocal(@Param("local") String local);

    // 2. 카테고리만을 선택했을 때 쿼리문
    @Query( nativeQuery = true , value = "select * from room where roomCategory = :category")
    List<RoomEntity> findRoomByCategory(@Param("category") String category);

    // 3. 지역, 카테고리 동시 선택 시 쿼리문
    @Query(nativeQuery = true, value= "select * from room where roomLocal = (select roomLocal from room where roomCategory =: category) ")
    List<RoomEntity> findRoomByLocalAndCategory(@Param("local") String local, @Param("category") String category);

}

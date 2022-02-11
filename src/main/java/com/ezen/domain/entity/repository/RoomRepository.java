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
    @Query(nativeQuery = true, value = "select * from (select * from room where roomCategory = :category) as R where roomLocal = :local")
    List<RoomEntity> findRoomByLocalAndCategory(@Param("local") String local, @Param("category") String category);

    // 4. 검색이 존재할 때
    // 4.1 검색 O 지역 X 카테고리 X
    @Query(nativeQuery = true, value = "select * from room where roomLocal like %:keyword% OR roomCategory like %:keyword% OR roomTitle like %:keyword% OR roomContent like %:keyword%")
    List<RoomEntity> findRoomByKeyword(@Param("keyword") String keyword);

    // 4.2 검색 O 지역 O 카테고리 X

    // 4.3 검색 O 지역 X 카테고리 O

    // 4.4 검색 O 지역 O 카테고리 O

    // 5. roomDate 로 Room 엔티티 찾기
    @Query(nativeQuery = true, value="select * from room where roomDate = :roomDate")
    List<RoomEntity> findRoomByRoomDate(@Param("roomDate") String roomDate);


    // 6. roomNo 로

}

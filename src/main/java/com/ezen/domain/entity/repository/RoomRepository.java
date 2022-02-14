package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    // 0. 검색이 없을 때
        // 1. 지역만을 선택했을 때 쿼리문
        @Query(nativeQuery = true, value = "SELECT * FROM room WHERE roomLocal = :local")
        Page<RoomEntity> findRoomByLocal(@Param("local") String local, Pageable pageable);

        // 2. 카테고리만을 선택했을 때 쿼리문
        @Query(nativeQuery = true, value = "SELECT * FROM room WHERE roomCategory = :category")
        Page<RoomEntity> findRoomByCategory(@Param("category") String category,  Pageable pageable);

        // 3. 지역, 카테고리 동시 선택 시 쿼리문
        @Query(nativeQuery = true, value = "SELECT * FROM (SELECT * FROM room WHERE roomCategory = :category) as R WHERE roomLocal = :local")
        Page<RoomEntity> findRoomByLocalAndCategory(@Param("local") String local, @Param("category") String category,  Pageable pageable);

    // 4. 검색이 존재할 때
        // 4.1 검색 O 지역 X 카테고리 X
        @Query(nativeQuery = true, value = "SELECT * FROM room WHERE roomLocal like %:keyword% OR roomCategory like %:keyword% OR roomTitle like %:keyword% OR roomContent like %:keyword%")
        Page<RoomEntity> findRoomByKeyword(@Param("keyword") String keyword,  Pageable pageable);

            // 검색 O 지역 O 카테고리 X 인 경우 쿼리문
            String localAndkeyword = "SELECT * FROM room where roomLocal = :local OR "
                    + "roomLocal like %:keyword% OR "
                    + "roomTitle like %:keyword% OR "
                    + "roomContent like %:keyword% OR "
                    + "roomCategory like %:keyword% OR "
                    + "roomDetail like %:keyword%";

        // 4.2 검색 O 지역 O 카테고리 X
        @Query(nativeQuery = true, value = localAndkeyword)
        Page<RoomEntity> findRoomByLocalAndKeyword(@Param("keyword") String keyword, @Param("local") String local, Pageable pageable);

            // 검색 O 지역 X 카테고리 O 인 경우 쿼리문
            String categoryAndkeyword = "SELECT * FROM room where roomCategory = :category OR "
                    + "roomCategory like %:keyword% OR "
                    + "roomTitle like %:keyword% OR "
                    + "roomContent like %:keyword% OR "
                    + "roomCategory like %:keyword% OR "
                    + "roomDetail like %:keyword%";

        // 4.3 검색 O 지역 X 카테고리 O
        @Query(nativeQuery = true, value = categoryAndkeyword)
        Page<RoomEntity> findRoomByCategoryAndKeyword(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

            // 검색 O 지역 O 카테고리 O 인 경우 쿼리문
            String categoryAndlocalAndkeyword = "SELECT * FROM room where roomCategory = :category OR "
                    + "roomCategory like %:keyword% OR "
                    + "roomLocal = :local OR "
                    + "roomLocal like %:keyword% OR "
                    + "roomTitle like %:keyword% OR "
                    + "roomContent like %:keyword% OR "
                    + "roomCategory like %:keyword% OR "
                    + "roomDetail like %:keyword%";

        // 4.4 검색 O 지역 O 카테고리 O
        @Query(nativeQuery = true, value=categoryAndlocalAndkeyword)
        Page<RoomEntity> findRoomByCategoryAndLocalAndKeyword(@Param("keyword") String keyword, @Param("category") String category, @Param("local") String local,  Pageable pageable);

    // 5. roomDate 로 Room 엔티티 찾기
    @Query(nativeQuery = true, value="SELECT * FROM room WHERE roomDate = :roomDate")
    List<RoomEntity> findRoomByRoomDate(@Param("roomDate") String roomDate);


    // 6. roomNo 로

}

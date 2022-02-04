package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    // JPA 메소드 만들기
    //  네이티브쿼리 = 실제SQL


//    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search% or roomTitle like %:search%"  )
//    Page<RoomEntity> findAllnone( @Param("search") String search, Pageable pageable);
/*    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllhandmade( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllcooking( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllflower( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAlldrawing( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllmusic( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllyoga( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllleisure( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllbeauty( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllexperience( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAlldevelopment( @Param("search") String search, Pageable pageable);
    @Query( nativeQuery = true , value = "select * from room where roomCategory like %:search%" )
    Page<RoomEntity> findAllpet( @Param("search") String search, Pageable pageable);*/

}

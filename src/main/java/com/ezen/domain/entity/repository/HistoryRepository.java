package com.ezen.domain.entity.repository;

import com.ezen.domain.dto.StatsDto;
import com.ezen.domain.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {
    // 1. memberNo 를 사용해 특정 회원의 예약 내역을 가져오기
    @Query(nativeQuery = true, value ="SELECT * FROM history WHERE memberNo = :memberNo")
    List<HistoryEntity> getHistoryByMemberNo(@Param("memberNo") int memberNo);

    // 2. (inner) JOIN 사용해서 timetable 에 있는 값들을 합친다.
    @Query(nativeQuery = true, value ="SELECT H.memberNo, H.roomNo, H.historyPoint, H.createdDate, T.roomDate, T.roomTime FROM history as H JOIN timetable AS T ON H.timeTableNo = T.timeTableNo")
    List<StatsDto> getHistoryDtos();

}

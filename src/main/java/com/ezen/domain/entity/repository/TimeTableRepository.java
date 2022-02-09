package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.TimeTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTableEntity, Integer> {

    // 1. 가장 최근 9개의 강좌 목록을 호출합니다.
    // 1.1 중복은 허용하지 않습니다.
    // 1.2 중복이 있을 수도 있기 때문에 limit 9 는 사용하지 않습니다.
    // 1.2.1 service 에서 제어
    @Query(nativeQuery = true, value = "select * from timetable order by createdDate desc")
    List<TimeTableEntity> getByTimeSequence();
}

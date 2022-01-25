package com.ezen.domain.entity.repository;

import com.ezen.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository <MemberEntity, Integer> {
}

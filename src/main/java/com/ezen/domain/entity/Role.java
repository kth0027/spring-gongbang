package com.ezen.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", "관리자"), MEMBER("ROLE_MEMBER", "일반회원"), STAFF("ROLE_STAFF", "스태프");

    private final String key;
    private final String role;
}

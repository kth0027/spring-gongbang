package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int m_no;

    @Column(name = "m_id")
    private String m_id;
}

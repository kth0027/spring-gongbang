package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int BoardNo;

}

package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roomlike")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomLikeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int rookLikeNo;
}

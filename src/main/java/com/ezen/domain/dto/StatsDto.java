package com.ezen.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StatsDto {

    private int memberNo;
    private int roomNo;
    private int historyPoint;
    private String roomDate;
    private int beginTime;
    private int endTime;
    private String category;
    private String local;
    private String createdDate;
    private int price;
    private int person;

}

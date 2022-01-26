package com.ezen.domain.dto;

import com.ezen.domain.entity.RoomEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {

    private int roomNo;
    private String roomTitle;
    private String roomCategory;
    private String roomContent;
    private String roomDetail;
    private String roomDate;
    private String roomAddress;
    private String roomLocal;
    private int roomStatus;
    private int roomMax;

    public RoomEntity toEntity(){
        return RoomEntity.builder()
                .roomTitle(this.roomTitle)
                .roomCategory(this.roomCategory)
                .roomAddress(this.roomAddress)
                .roomContent(this.roomContent)
                .roomDate(this.roomDate)
                .roomStatus(0)
                .roomMax(2)
                .roomDetail(this.roomDetail)
                .build();
    }

}

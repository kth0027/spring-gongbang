package com.ezen.service;


import com.ezen.domain.entity.repository.RoomLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomLikeService {

    @Autowired
    RoomLikeRepository roomLikeRepository;
}

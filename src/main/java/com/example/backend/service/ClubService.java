package com.example.backend.service;

import com.example.backend.dto.ClubDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubService {
    void register(Long memberId, ClubDTO clubDTO);
    ClubDTO findClubById(Long clubId);
    ClubDTO findClubByName(String clubName);
    Page<ClubDTO> findAllClubs(Pageable pageable);
    ClubDTO modify(Long clubId, ClubDTO clubDTO, Long currentUserId);
    void remove(Long clubId, Long currentUserId);
}

package com.example.backend.service;

import com.example.backend.dto.MembershipDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MembershipService {
    void addMembership(Long clubId, Long memberId, MembershipDTO membershipDTO);
    MembershipDTO findMembership(Long clubId, Long memberId);
    Page<MembershipDTO> findAllMembershipsByClub(Long clubId, Pageable pageable);
    Page<MembershipDTO> findAllMembershipsByMember(Long currentUserId, Pageable pageable);
    MembershipDTO modify(Long clubId, Long memberId, MembershipDTO membershipDTO);
    void remove(Long clubId, Long currentUserId);
    void removeByPresident(Long membershipId);
}

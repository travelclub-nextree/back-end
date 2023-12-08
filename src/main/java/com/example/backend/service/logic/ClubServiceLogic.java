package com.example.backend.service.logic;

import com.example.backend.dto.ClubDTO;
import com.example.backend.entity.Club;
import com.example.backend.entity.Member;
import com.example.backend.entity.Membership;
import com.example.backend.entity.Role;
import com.example.backend.service.ClubService;
import com.example.backend.store.ClubStore;
import com.example.backend.store.MemberStore;
import com.example.backend.store.MembershipStore;
import com.example.backend.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubServiceLogic implements ClubService {
    private final ClubStore clubStore;
    private final MemberStore memberStore;
    private final MembershipStore membershipStore;

    @Override
    @Transactional
    public void register(Long memberId, ClubDTO clubDTO) {
        Optional.ofNullable(clubStore.findByClubName(clubDTO.getClubName()))
                .ifPresent(dto -> {throw new ClubDuplicationException("Club already exists with name : " + clubDTO.getClubName());});
        Club club = clubDTO.DTOToEntity();
        Club savedClub = clubStore.save(club);

        Member member = memberStore.findById(memberId)
                .orElseThrow(() -> new NoSuchMemberException("No such member with id: " + memberId));
        Membership membership = new Membership();
        membership.setMember(member);
        membership.setClub(savedClub);
        membership.setRole(Role.PRESIDENT);
        member.getMemberships().add(membership);
        membershipStore.save(membership);
    }

    @Override
    public ClubDTO findClubById(Long clubId) {
        return clubStore.findById(clubId)
                .map(club -> new ClubDTO(club))
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));
    }

    @Override
    public ClubDTO findClubByName(String clubName) {
        return Optional.ofNullable(clubStore.findByClubName(clubName))
                .map(club -> new ClubDTO(club))
                .orElseThrow(() -> new NoSuchClubException("No such club with name : " + clubName));
    }

    @Override
    public Page<ClubDTO> findAllClubs(Pageable pageable) {
        Pageable sortedByCreatedTime = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdTime").descending());

        return clubStore.findAll(sortedByCreatedTime)
                .map(ClubDTO::new);
    }

    @Override
    @Transactional
    public ClubDTO modify(Long clubId, ClubDTO clubDTO, Long currentUserId) {
        Optional.ofNullable(clubStore.findByClubName(clubDTO.getClubName()))
                .ifPresent(club -> { throw new ClubDuplicationException("Club already exists with name : " + clubDTO.getClubName());});
        Club targetClub = clubStore.findById(clubId)
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));
        Role currentUserRole = getCurrentUserRoleInClub(clubId, currentUserId);

        if (currentUserRole == Role.PRESIDENT) {
            targetClub.setClubName(clubDTO.getClubName());
            targetClub.setClubIntro(clubDTO.getClubIntro());
        } else {
            throw new NoAuthorityForModifyClub("Does not have an authority to modify the club.");
        }

        return new ClubDTO(targetClub);
    }

    @Override
    @Transactional
    public void remove(Long clubId, Long currentUserId) {
        Club club = clubStore.findById(clubId)
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));
        Role currentUserRole = getCurrentUserRoleInClub(clubId, currentUserId);

        if (currentUserRole == Role.PRESIDENT) {
            clubStore.delete(club);
        } else {
            throw new NoAuthorityForDeleteClub("Does not have an authority to delete the club.");
        }
    }

    private Role getCurrentUserRoleInClub(Long clubId, Long currentUserId) {
        Club club = clubStore.findById(clubId)
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));

        return club.getMemberships().stream()
                .filter(membership -> membership.getMember().getMemberId() == currentUserId)
                .findFirst()
                .map(Membership::getRole)
                .orElseThrow(() -> new NotInClubException("Current User is not in this club."));
    }
}

package com.example.backend.controller;

import com.example.backend.dto.MembershipDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.service.MembershipService;
import com.example.backend.util.MemberDuplicationException;
import com.example.backend.util.NoSuchClubException;
import com.example.backend.util.NoSuchMemberException;
import com.example.backend.util.NoSuchMembershipException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/membership")
public class MembershipController {
    private final MembershipService membershipService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> joinClub(@RequestParam("clubId") Long clubId,
                                      @RequestBody MembershipDTO membershipDTO,
                                      HttpServletRequest request) {
        ResponseDTO<MembershipDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            membershipService.addMembership(clubId, currentUserId, membershipDTO);
            responseDTO.setItem(membershipDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchClubException | NoSuchMemberException | MemberDuplicationException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<?> searchMembership(@RequestParam("clubId") Long clubId,
                                              HttpServletRequest request) {
        ResponseDTO<MembershipDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            MembershipDTO foundMembership = membershipService.findMembership(clubId, currentUserId);
            responseDTO.setItem(foundMembership);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchMembershipException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/club")
    public ResponseEntity<?> searchMembershipsInClub(@RequestParam("clubId") Long clubId,
                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "5") int size) {
        ResponseDTO<MembershipDTO> responseDTO = new ResponseDTO<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MembershipDTO> foundMemberships = membershipService.findAllMembershipsByClub(clubId, pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(foundMemberships.getTotalPages());
            paginationInfo.setCurrentPage(foundMemberships.getNumber());
            paginationInfo.setTotalElements(foundMemberships.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setItems(foundMemberships.getContent());
            responseDTO.setLastPage(foundMemberships.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchClubException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/member")
    public ResponseEntity<?> searchMembershipsInMember(HttpServletRequest request,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        ResponseDTO<MembershipDTO> responseDTO = new ResponseDTO<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            Page<MembershipDTO> foundMemberships = membershipService.findAllMembershipsByMember(currentUserId, pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(foundMemberships.getTotalPages());
            paginationInfo.setCurrentPage(foundMemberships.getNumber());
            paginationInfo.setTotalElements(foundMemberships.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setItems(foundMemberships.getContent());
            responseDTO.setLastPage(foundMemberships.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchMemberException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateMembership(@RequestParam("clubId") Long clubId,
                                              @RequestParam("memberId") Long memberId,
                                              @RequestBody MembershipDTO membershipDTO) {
        ResponseDTO<MembershipDTO> responseDTO = new ResponseDTO<>();
        try {
            MembershipDTO updatedMembershipDTO = membershipService.modify(clubId, memberId, membershipDTO);
            responseDTO.setItem(updatedMembershipDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchClubException | NoSuchMemberException | NoSuchMembershipException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMembership(@RequestParam("clubId") Long clubId,
                                              HttpServletRequest request) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            membershipService.remove(clubId, currentUserId);
            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("message", "Successfully removed the membership.");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchClubException | NoSuchMemberException | NoSuchMembershipException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMembershipByPresident(@RequestParam("membershipId") Long membershipId) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            membershipService.removeByPresident(membershipId);
            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("message", "Successfully removed the membership.");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchClubException | NoSuchMemberException | NoSuchMembershipException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}

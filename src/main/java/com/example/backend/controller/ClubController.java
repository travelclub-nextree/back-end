package com.example.backend.controller;

import com.example.backend.dto.ClubDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.service.ClubService;
import com.example.backend.util.ClubDuplicationException;
import com.example.backend.util.NoSuchClubException;
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
@RequestMapping("/api/club")
public class ClubController {
    private final ClubService clubService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> createClub(@RequestBody ClubDTO clubDTO,
                                        HttpServletRequest request) {
        ResponseDTO<ClubDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            clubService.register(currentUserId, clubDTO);
            responseDTO.setItem(clubDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (ClubDuplicationException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<?> searchByClubId(@PathVariable("clubId") Long clubId) {
        ResponseDTO<ClubDTO> responseDTO = new ResponseDTO<>();
        try {
            ClubDTO foundClubDTO = clubService.findClubById(clubId);
            responseDTO.setItem(foundClubDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchClubException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<?> searchByClubName(@RequestParam("clubName") String clubName) {
        ResponseDTO<ClubDTO> responseDTO = new ResponseDTO<>();
        try {
            ClubDTO foundClubDTO = clubService.findClubByName(clubName);
            responseDTO.setItem(foundClubDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchClubException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchAllClubs(@RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "5") int size) {
        ResponseDTO<ClubDTO> responseDTO = new ResponseDTO<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ClubDTO> clubPage = clubService.findAllClubs(pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(clubPage.getTotalPages());
            paginationInfo.setCurrentPage(clubPage.getNumber());
            paginationInfo.setTotalElements(clubPage.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setItems(clubPage.getContent());
            responseDTO.setLastPage(clubPage.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchClubException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateClub(@RequestParam("clubId") Long clubId,
                                        @RequestBody ClubDTO clubDTO,
                                        HttpServletRequest request) {
        ResponseDTO<ClubDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            ClubDTO updatedClubDTO = clubService.modify(clubId, clubDTO, currentUserId);
            responseDTO.setItem(updatedClubDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchClubException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable("clubId") Long clubId,
                                        HttpServletRequest request) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            clubService.remove(clubId, currentUserId);
            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("message", "successfully removed club with id : " + clubId);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchClubException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}

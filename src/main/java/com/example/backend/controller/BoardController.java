package com.example.backend.controller;

import com.example.backend.dto.BoardDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.service.BoardService;
import com.example.backend.util.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestParam("clubId") Long clubId,
                                         @RequestBody BoardDTO boardDTO,
                                         HttpServletRequest request) {
        ResponseDTO<BoardDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            boardService.register(clubId, boardDTO, currentUserId);
            responseDTO.setItem(boardDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchClubException | BoardLimitOverException | BoardDuplicationException | NoPermissionToCreateBoard e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> searchBoard(@PathVariable("boardId") Long boardId) {
        ResponseDTO<BoardDTO> responseDTO = new ResponseDTO<>();
        try {
            BoardDTO boardDTO = boardService.findBoard(boardId);
            responseDTO.setItem(boardDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchBoardException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<?> searchBoardByTitle(@RequestParam("clubId") Long clubId,
                                                @RequestParam("boardTitle") String boardTitle) {
        ResponseDTO<BoardDTO> responseDTO = new ResponseDTO<>();
        try {
            BoardDTO boardDTO = boardService.findByClubIdAndBoardTitle(clubId, boardTitle);
            responseDTO.setItem(boardDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchBoardException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchBoardByClubId(@RequestParam("clubId") Long clubId,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "5") int size) {
        ResponseDTO<BoardDTO> responseDTO = new ResponseDTO<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BoardDTO> boardPage = boardService.findByClubId(clubId, pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(boardPage.getTotalPages());
            paginationInfo.setCurrentPage(boardPage.getNumber());
            paginationInfo.setTotalElements(boardPage.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setItems(boardPage.getContent());
            responseDTO.setLastPage(boardPage.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchClubException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateBoard(@RequestParam("clubId") Long clubId,
                                         @RequestBody BoardDTO boardDTO,
                                         HttpServletRequest request) {
        ResponseDTO<BoardDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            boardService.modify(clubId, boardDTO, currentUserId);
            responseDTO.setItem(boardDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchClubException | NoPermissionToModifyBoard | NoSuchBoardException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBoard(@RequestParam("boardId") Long boardId,
                                         HttpServletRequest request) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            boardService.remove(boardId, currentUserId);
            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("message", "Successfully removed the board.");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchBoardException | NoPermisiionToDeleteBoard e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}

package com.example.backend.controller;

import com.example.backend.dto.PostDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.service.PostService;
import com.example.backend.util.NoAuthorityForDeletePost;
import com.example.backend.util.NoAuthorityForModifyPost;
import com.example.backend.util.NoSuchBoardException;
import com.example.backend.util.NoSuchPostingException;
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
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestParam("boardId") Long boardId,
                                        @RequestBody PostDTO postDTO,
                                        HttpServletRequest request) {
        ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            postService.register(boardId, postDTO, currentUserId);
            responseDTO.setItem(postDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchBoardException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> searchPost(@PathVariable("postId") Long postId) {
        ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>();
        try {
            PostDTO postDTO = postService.findPost(postId);
            responseDTO.setItem(postDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchPostingException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<?> searchByTitleInBoard(@RequestParam("boardId") Long boardId,
                                                  @RequestParam("postTitle") String postTitle,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size) {
        ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PostDTO> foundPosts = postService.findByTitleInBoard(boardId, postTitle, pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(foundPosts.getTotalPages());
            paginationInfo.setCurrentPage(foundPosts.getNumber());
            paginationInfo.setTotalElements(foundPosts.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setItems(foundPosts.getContent());
            responseDTO.setLastPage(foundPosts.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchBoardException | NoSuchPostingException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/list/{boardId}")
    public ResponseEntity<?> searchByBoard(@PathVariable("boardId") Long boardId,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "5") int size) {
        ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PostDTO> foundPosts = postService.findByBoard(boardId, pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(foundPosts.getTotalPages());
            paginationInfo.setCurrentPage(foundPosts.getNumber());
            paginationInfo.setTotalElements(foundPosts.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setItems(foundPosts.getContent());
            responseDTO.setLastPage(foundPosts.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchBoardException | NoSuchPostingException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchByClubAndMember(@RequestParam("clubId") Long clubId,
                                                   @RequestParam("memberId") Long memberId,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "5") int size) {
        ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PostDTO> foundPosts = postService.findByClubAndMember(clubId, memberId, pageable);

            ResponseDTO.PaginationInfo paginationInfo = new ResponseDTO.PaginationInfo();
            paginationInfo.setTotalPages(foundPosts.getTotalPages());
            paginationInfo.setCurrentPage(foundPosts.getNumber());
            paginationInfo.setTotalElements(foundPosts.getTotalElements());

            responseDTO.setPaginationInfo(paginationInfo);
            responseDTO.setItems(foundPosts.getContent());
            responseDTO.setLastPage(foundPosts.isLast());
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchBoardException | NoSuchPostingException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping
    public ResponseEntity<?> updatePost(@RequestParam("postId") Long postId,
                                        @RequestBody PostDTO postDTO,
                                        HttpServletRequest request) {
        ResponseDTO<PostDTO> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            postService.modify(postId, postDTO, currentUserId);
            responseDTO.setItem(postDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (NoSuchPostingException | NoAuthorityForModifyPost e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletePost(@RequestParam("postId") Long postId,
                                        HttpServletRequest request) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getMemberId(token);

            postService.remove(postId, currentUserId);
            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("message", "Successfully removed the post.");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchPostingException | NoAuthorityForDeletePost e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}

package com.example.backend.service;

import com.example.backend.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    void register(Long boardId, PostDTO postDTO, Long currentUserId);
    PostDTO findPost(Long postId);
    Page<PostDTO> findByTitleInBoard(Long boardId, String postTitle, Pageable pageable);
    Page<PostDTO> findByBoard(Long boardId, Pageable pageable);
    Page<PostDTO> findByClubAndMember(Long clubId, Long memberId, Pageable pageable);
    PostDTO modify(Long postId, PostDTO postDTO, Long currentUserId);
    void remove(Long postId, Long currentUserId);
}

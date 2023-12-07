package com.example.backend.service;

import com.example.backend.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    void register(Long clubId, BoardDTO boardDTO, Long currentUserId);
    BoardDTO findBoard(Long boardId);
    BoardDTO findByClubIdAndBoardTitle(Long clubId, String boardTitle);
    Page<BoardDTO> findByClubId(Long clubId, Pageable pageable);
    BoardDTO modify(Long clubId, BoardDTO boardDTO, Long currentUserId);
    void remove(Long boardId, Long currentUserId);
}

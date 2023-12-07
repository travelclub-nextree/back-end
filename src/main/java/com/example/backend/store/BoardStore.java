package com.example.backend.store;

import com.example.backend.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardStore extends JpaRepository<Board, Long> {
    Board findByClub_ClubIdAndBoardTitle(Long clubId, String boardTitle);

    Page<Board> findByClub_ClubId(Long clubId, Pageable pageable);
}

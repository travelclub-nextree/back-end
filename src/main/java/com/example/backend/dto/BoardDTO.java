package com.example.backend.dto;

import com.example.backend.entity.Board;
import com.example.backend.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private long boardId;

    private String boardTitle;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private long clubId;

    public BoardDTO(String boardTitle) {
        this();
        this.boardTitle = boardTitle;
    }

    public BoardDTO(Board board) {
        this(board.getBoardTitle());
        this.createdTime = board.getCreatedTime();
        this.updatedTime = board.getUpdatedTime();
        this.clubId = board.getClub().getClubId();
    }

    public Board DTOToEntity() {
        return Board.builder()
                .boardId(this.boardId)
                .boardTitle(this.boardTitle)
                .club(Club.builder()
                        .clubId(this.clubId)
                        .build())
                .build();
    }
}

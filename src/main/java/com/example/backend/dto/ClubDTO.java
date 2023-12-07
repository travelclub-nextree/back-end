package com.example.backend.dto;

import com.example.backend.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class ClubDTO {
    private long clubId;

    private String clubName;

    private String clubIntro;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private long boardCount;

    private List<MembershipDTO> memberships;

    private List<BoardDTO> boards;

    private ClubDTO() {
        this.memberships = new ArrayList<MembershipDTO>();
    }

    public ClubDTO(String clubName, String clubIntro) {
        this();
        this.clubName = clubName;
        this.clubIntro = clubIntro;
    }

    public ClubDTO(Club club) {
        this(club.getClubName(), club.getClubIntro());
        this.clubId = club.getClubId();
        this.createdTime = club.getCreatedTime();
        this.updatedTime = club.getUpdatedTime();
        this.boardCount = club.getBoardCount();

        this.memberships = Optional.ofNullable(club.getMemberships())
                .orElse(Collections.emptyList())
                .stream()
                .map(MembershipDTO::new)
                .collect(Collectors.toList());

        this.boards = Optional.ofNullable(club.getBoards())
                .orElse(Collections.emptyList())
                .stream()
                .map(BoardDTO::new)
                .collect(Collectors.toList());
    }

    public Club DTOToEntity() {
        return Club.builder()
                .clubId(this.clubId)
                .clubName(this.clubName)
                .clubIntro(this.clubIntro)
                .boardCount(this.boardCount)
                .build();
    }
}

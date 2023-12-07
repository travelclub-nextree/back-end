package com.example.backend.entity;

import com.example.backend.dto.BoardDTO;
import com.example.backend.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOARD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", nullable = false)
    private long boardId;

    @Column(name = "BOARD_TITLE", nullable = false)
    @Size(min = 3)
    private String boardTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public BoardDTO EntityToDTO() {
        return BoardDTO.builder()
                .boardId(this.boardId)
                .boardTitle(this.boardTitle)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .clubId(this.club.getClubId())
                .build();
    }
}

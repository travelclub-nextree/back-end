package com.example.backend.entity;

import com.example.backend.dto.ClubDTO;
import com.example.backend.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CLUB")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Club extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUB_ID", nullable = false)
    private long clubId;

    @Column(name = "CLUB_NAME", nullable = false)
    @Size(min = 3)
    private String clubName;

    @Column(name = "CLUB_INTRO", nullable = false)
    @Size(min = 10)
    private String clubIntro;

    @Column(name = "BOARD_COUNT")
    @Min(0)
    @Max(5)
    private long boardCount;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public ClubDTO EntityToDTO() {
        return ClubDTO.builder()
                .clubId(this.clubId)
                .clubName(this.clubName)
                .clubIntro(this.clubIntro)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .boardCount(this.getBoardCount())
                .build();
    }
}

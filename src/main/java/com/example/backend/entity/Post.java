package com.example.backend.entity;

import com.example.backend.dto.PostDTO;
import com.example.backend.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "POST")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID", nullable = false)
    private long postId;

    @Column(name = "POST_TITLE", nullable = false)
    private String postTitle;

    @Column(name = "POST_CONTENT", nullable = false)
    private String postContent;

    @Column(name = "POST_VIEW_COUNT", nullable = false)
    private long postViewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public PostDTO EntityToDTO() {
        return PostDTO.builder()
                .postId(this.postId)
                .postTitle(this.postTitle)
                .postContent(this.postContent)
                .postViewCount(this.postViewCount)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .boardId(this.board.getBoardId())
                .memberId(this.member.getMemberId())
                .memberNickname(this.member.getMemberNickname())
                .build();
    }
}

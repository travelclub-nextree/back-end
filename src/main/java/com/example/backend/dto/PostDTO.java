package com.example.backend.dto;

import com.example.backend.entity.Board;
import com.example.backend.entity.Club;
import com.example.backend.entity.Member;
import com.example.backend.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private long postId;

    private String postTitle;

    private String postContent;

    private long postViewCount;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private long boardId;

    private long memberId;

    private String memberNickname;

    public PostDTO(String postTitle, String postContent) {
        this();
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public PostDTO(Post post) {
        this(post.getPostTitle(), post.getPostContent());
        this.postViewCount = post.getPostViewCount();
        this.createdTime = post.getCreatedTime();
        this.updatedTime = post.getUpdatedTime();
        this.boardId = post.getBoard().getBoardId();
        this.memberId = post.getMember().getMemberId();
        this.memberNickname = post.getMember().getMemberNickname();
    }

    public Post DTOToEntity() {
        return Post.builder()
                .postId(this.postId)
                .postTitle(this.postTitle)
                .postContent(this.postContent)
                .postViewCount(this.postViewCount)
                .board(Board.builder()
                        .boardId(this.boardId)
                        .build())
                .member(Member.builder()
                        .memberId(this.memberId)
                        .build())
                .build();
    }
}

package com.example.backend.store;

import com.example.backend.entity.Club;
import com.example.backend.entity.Member;
import com.example.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStore extends JpaRepository<Post, Long> {
    Page<Post> findByBoard_BoardIdAndPostTitle(Long boardId, String postTitle, Pageable pageable);
    Page<Post> findByBoard_BoardId(Long boardId, Pageable pageable);
    Page<Post> findByBoard_ClubAndMember(Club club, Member member, Pageable pageable);
}

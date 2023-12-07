package com.example.backend.store;

import com.example.backend.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStore extends JpaRepository<Member, Long> {
    Member findByMemberEmail(String memberEmail);

    Page<Member> findMembersByMemberNickname(String memberNickname, Pageable pageable);
}

package com.example.backend.store;

import com.example.backend.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubStore extends JpaRepository<Club, Long> {
    Club findByClubName(String clubName);
}

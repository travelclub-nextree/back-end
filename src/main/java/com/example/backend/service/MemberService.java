package com.example.backend.service;

import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.MemberDTO;
import com.example.backend.entity.Member;
import com.example.backend.util.InvalidEmailException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    void register(MemberDTO memberDTO) throws InvalidEmailException;
    MemberDTO find(String memberEmail);
    MemberDTO findMember(Long memberId);
    Page<MemberDTO> findByNickname(String memberNickname, Pageable pageable);
    MemberDTO modify(Long memberId, MemberDTO memberDTO) throws InvalidEmailException;
    void remove(Long memberId);
    Member login(LoginDTO loginDTO);
}

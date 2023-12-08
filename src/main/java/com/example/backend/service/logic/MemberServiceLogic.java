package com.example.backend.service.logic;

import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.MemberDTO;
import com.example.backend.entity.Member;
import com.example.backend.service.MemberService;
import com.example.backend.store.MemberStore;
import com.example.backend.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceLogic implements MemberService {
    private final MemberStore memberStore;

    @Override
    @Transactional
    public void register(MemberDTO memberDTO) throws InvalidEmailException {
        String email = memberDTO.getMemberEmail();
        Optional.ofNullable(memberStore.findByMemberEmail(email))
                .ifPresent(member -> {throw new MemberDuplicationException("Member already exists with email : " + member.getMemberEmail());});
        memberStore.save(memberDTO.DTOToEntity());
    }

    @Override
    public MemberDTO find(String memberEmail) {
        return Optional.ofNullable(memberStore.findByMemberEmail(memberEmail))
                .map(member -> new MemberDTO(member))
                .orElseThrow(() -> new NoSuchMemberException("No such member with email : " + memberEmail));
    }

    @Override
    public MemberDTO findMember(Long memberId) {
         return memberStore.findById(memberId)
                 .map(member -> new MemberDTO(member))
                 .orElseThrow(() -> new NoSuchMemberException("No such member with id : " + memberId));
    }

    @Override
    public Page<MemberDTO> findByNickname(String memberNickname, Pageable pageable) {
        Pageable sortedByCreatedTime = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdTime").descending());

        Page<Member> members = memberStore.findMembersByMemberNickname(memberNickname, sortedByCreatedTime);
        if (members.isEmpty()) {
            throw new NoSuchMemberException("No such member with nickname : " + memberNickname);
        }

        return members.map(MemberDTO::new);
    }

    @Override
    @Transactional
    public MemberDTO modify(Long memberId, MemberDTO memberDTO) throws InvalidEmailException {
        Optional.ofNullable(memberStore.findByMemberEmail(memberDTO.getMemberEmail()))
                .orElseThrow(() -> new NoSuchMemberException("No such member with email : " + memberDTO.getMemberEmail()));

        Member targetMember = memberStore.findById(memberId)
                        .orElseThrow(() -> new NoSuchMemberException("No such member with id : " + memberId));

        targetMember.setMemberNickname(memberDTO.getMemberNickname());
        targetMember.setMemberTel(memberDTO.getMemberTel());
        targetMember.setMemberZipcode(memberDTO.getMemberZipcode());
        targetMember.setMemberAddress(memberDTO.getMemberAddress());
        targetMember.setMemberDetailAddress(memberDTO.getMemberDetailAddress());

        return new MemberDTO(targetMember);
    }

    @Override
    @Transactional
    public void remove(Long memberId) {
        Member member = memberStore.findById(memberId)
                .orElseThrow(() -> new NoSuchMemberException("No such member with id: " + memberId));

        memberStore.delete(member);
    }

    @Override
    public Member login(LoginDTO loginDTO) {
        Member loginUser = Optional.ofNullable(memberStore.findByMemberEmail(loginDTO.getMemberEmail()))
                .orElseThrow(() -> new NoSuchEmailException("User Email or password is wrong."));

        if (!loginUser.getMemberPassword().equals(loginDTO.getMemberPassword())) {
            throw new PasswordNotMatchException("User Email or password is wrong.");
        }

        return loginUser;
    }
}

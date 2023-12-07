package com.example.backend.dto;

import com.example.backend.entity.Member;
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
public class MemberDTO {
    private long memberId;

    private String memberEmail;

    private String memberPassword;

    private String memberNickname;

    private String memberTel;

    private String memberZipcode;

    private String memberAddress;

    private String memberDetailAddress;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private List<MembershipDTO> memberships;

    public MemberDTO() {
        this.memberships = new ArrayList<MembershipDTO>();
    }

    public MemberDTO(String memberEmail, String memberNickname, String memberTel) {
        this();
        this.memberEmail = memberEmail;
        this.memberNickname = memberNickname;
        this.memberTel = memberTel;
    }

    public MemberDTO(Member member) {
        this(member.getMemberEmail(), member.getMemberNickname(), member.getMemberTel());
        this.memberZipcode = member.getMemberZipcode();
        this.memberAddress = member.getMemberAddress();
        this.memberDetailAddress = member.getMemberDetailAddress();
        this.createdTime = member.getCreatedTime();
        this.updatedTime = member.getUpdatedTime();

        this.memberships = Optional.ofNullable(member.getMemberships())
                .orElse(Collections.emptyList())
                .stream()
                .map(MembershipDTO::new)
                .collect(Collectors.toList());
    }

    public Member DTOToEntity() {
        return Member.builder()
                .memberId(this.memberId)
                .memberEmail(this.memberEmail)
                .memberPassword(this.memberPassword)
                .memberNickname(this.memberNickname)
                .memberTel(this.memberTel)
                .memberZipcode(this.memberZipcode)
                .memberAddress(this.memberAddress)
                .memberDetailAddress(this.memberDetailAddress)
                .build();
    }
}

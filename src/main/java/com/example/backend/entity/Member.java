package com.example.backend.entity;

import com.example.backend.dto.MemberDTO;
import com.example.backend.util.BaseTimeEntity;
import com.example.backend.util.InvalidEmailException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", nullable = false)
    private long memberId;

    @Column(name = "MEMBER_EMAIL", nullable = false)
    private String memberEmail;

    @Column(name = "MEMBER_PASSWORD", nullable = false)
    private String memberPassword;

    @Column(name = "MEMBER_NICKNAME", nullable = false)
    private String memberNickname;

    @Column(name = "MEMBER_TEL")
    private String memberTel;

    @Column(name = "MEMBER_ZIPCODE")
    private String memberZipcode;

    @Column(name = "MEMBER_ADDRESS")
    private String memberAddress;

    @Column(name = "MEMBER_DETAIL_ADDRESS")
    private String memberDetailAddress;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    public MemberDTO EntityToDTO() {
        return MemberDTO.builder()
                .memberId(this.memberId)
                .memberEmail(this.memberEmail)
                .memberPassword(this.memberPassword)
                .memberNickname(this.memberNickname)
                .memberTel(this.memberTel)
                .memberZipcode(this.memberZipcode)
                .memberAddress(this.memberAddress)
                .memberDetailAddress(this.memberDetailAddress)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public Member(String memberEmail, String memberPassword, String memberNickname) throws InvalidEmailException {
        this();
        setMemberEmail(memberEmail);
        this.memberPassword = memberPassword;
        this.memberNickname = memberNickname;
    }
}

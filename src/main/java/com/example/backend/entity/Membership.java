package com.example.backend.entity;

import com.example.backend.dto.MembershipDTO;
import com.example.backend.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBERSHIP")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBERSHIP_ID", nullable = false)
    private long membershipId;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    public MembershipDTO EntityToDTO() {
        return MembershipDTO.builder()
                .membershipId(membershipId)
                .role(this.role.toString())
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .memberId(this.member.getMemberId())
                .clubId(this.club.getClubId())
                .build();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("membershipId=").append(membershipId);
        builder.append(", role=").append(role.name());
        builder.append(", memberId=").append(member != null ? member.getMemberId() : null);
        builder.append(", clubId=").append(club != null ? club.getClubId() : null);
        builder.append(", createdDate").append(getCreatedTime());
        builder.append(", updatedDtate").append(getUpdatedTime());

        return builder().toString();
    }
}

package com.example.backend.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   //BaseTimeEntity를 상속한 엔티티는 부모 클래스의 필드를 컬럼으로 인식한다.
@EntityListeners(AuditingEntityListener.class)  //Auditing(자동으로 값 매핑) 기능 추가
public class BaseTimeEntity {
    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedTime;
}

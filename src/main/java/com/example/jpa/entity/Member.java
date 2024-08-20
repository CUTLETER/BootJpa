package com.example.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // JPA 엔터티를 관리하는 영역에서 자동으로 변수에 적용시켜줌
// Auditing 기능을 활성화 시키려면 main 클래스(BootJpaApplication 파일)에 @EnableJpaAuditing를 추가해야 함
public class Member {
    @Id // PK
    private String id;
    @Column(nullable = false, length = 50) // null 허용 불가, 길이 50
    private String name;

    @CreatedDate // JPA를 통해서 insert 작업 시 날짜가 자동 입력됨
    @Column(updatable = false) // JPA로 인한 자동 업데이트 방지
    private LocalDate signDate;
}

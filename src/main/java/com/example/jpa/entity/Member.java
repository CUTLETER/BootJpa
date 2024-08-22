package com.example.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
// @ToString - 양방향 맵핑 시 ToString은 한쪽 엔터티에만 있어야 함, 아니면 둘이 번갈아가며 쓰려해서 오류남
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

    // 1:N
    // One To Many 방식의 조인
    // @JoinColumn 달아주지 않으면
    // 연관 관계의 주인인 1의 입장이 되면서 쓸데없는 맵핑 테이블 자동 생성시킴
    // * * 연관 관계의 주인이란? FK를 관리하는 주체
    // 예) member_list 라는 맵핑 테이블이 멋대로 생겨버림
    //@OneToMany (fetch = FetchType.EAGER) // One To Many 기본 방식은 Lazy
    //@JoinColumn(name = "member_id") // N 테이블의 member_id를 FK 삼아 조인함 (꼭 적어야 함)
    //private final List<Memo> list = new ArrayList<>();

    // 양방향 맵핑
    @OneToMany (mappedBy= "member") // mappedBy는 1쪽 엔터티에서 지정하고, 연관관계의 주인을 나타냄
    private final List<Memo> list = new ArrayList<>();
}

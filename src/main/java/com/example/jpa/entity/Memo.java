package com.example.jpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity // JPA가 엔터티로 관리한다는 의미 - 영속성 컨텍스트(Persistence Context)에 등록시킴
@Table(name="MEMO") // 메모 테이블
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Memo {
//    엔터티를 정의하면 하이버네이트가 DDL 구문을 대신 실행시켜줌, spring.jpa.hibernate.ddl-auto=update 이 옵션이 변경 사항을 자동 업데이트 시켜줌

//    시퀀스가 존재하는 '오라클'은 아래처럼
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "연결시킬 변수명") // 시퀀스를 생성함
//    @SequenceGenerator(name = "연결시킬 변수명", sequenceName = "시퀀스명", initialValue = 1, allocationSize = 1) // 시퀀스명, 시작값, 증가값 지정

//  시퀀스가 존재하지 않는 'MySQL'은 아래처럼
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment가 동작됨
    @Id // mno 롤 PK로 쓰겠다는 의미
    private long mno;
    @Column(length = 200, nullable = false) // 길이 200 에 null 허용 '불가'
    private String writer;
    @Column(columnDefinition = "varchar(200) default 'y'") // 만들고 싶은 컬럼의 제약조건을 직접 명시함
    private String text;

    // N:1
    // FK 컬럼명을 명시하지 않으면 Member 엔터티에 member_Pk로 자동 생성됨
    //@ManyToOne (fetch = FetchType.LAZY) // 기본 방식은 Eager
    //@JoinColumn(name = "member_id") // Member 엔터티의 PK를 member_id 컬럼에 저장시킴
    //private Member member; //멤버 엔터티

    // 양방향 맵핑
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


} // sql문을 따로 적지 않았음에도 서버 실행과 동시에 memo 테이블이 자동으로 생성됨

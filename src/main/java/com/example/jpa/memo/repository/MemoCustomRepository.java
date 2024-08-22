package com.example.jpa.memo.repository;

import com.example.jpa.entity.Member;
import com.example.jpa.entity.MemberMemoDTO;
import com.example.jpa.entity.Memo;
import com.example.jpa.util.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemoCustomRepository {

    public int updateTest(String writer,long mno);

    List<Memo> mtoJoin1(Long mno); // Many To One 조인 (N + 1 문제) - 조인을 하기 위해 추가적인 select 구문을 날리는 행위

    List<Object[]> mtoJoin2(Long mno); // 두 개 이상의 엔터티를 동시에 조회하기 (반환 타입을 object[]로)

    List<Object[]> mtoJoin3(String writer); // 연관 관계가 없는 엔터티의 조인 (비식별 관계), (위에랑은 다르게 FK 제약 XX)

    List<Memo> mtoJoin4(); // 성능 향상 Fetch 조인 (로딩 전략보다 무조건 우선 시 적용됨)

    /////////////////////

    // One To Many 방식
    Member otmJoin1 (String id); // One To Many 조인

    List<Member> otmJoin2 (String id); // Fetch 조인 - 1:N 조인에서 Fetch 방식은 중복 행을 생성함! (sql문에 꼭 'distinct' 넣어줘야 함)


    /////////////////////
    // 다대일 양방향 맵핑 사용할 것

    // DTO로 반환 받기
    List<MemberMemoDTO> otmJoin3(String id);

    //List<MemberMemoDTO> otmJoin4(String writer);

    // select * from memo m left join member x on m.member_id = x.id where m.writer like '%1%';
    // jpql로 사용하고, memberMemoDTO 타입으로 반환 받는 구문 작성하기
    List<MemberMemoDTO> otmJoin4(String text);

    // otmJoin4에다가 페이징 처리 기능 추가
    // 조인된 결과를 Pageable처리
    // Pageable 새로 추가함, List 타입에서 Page 타입으로 바꿈
    // Page<MemberMemoDTO> joinPage(String text, Pageable pageable);


    //////////////////////////////////

    // 쿼리 DSL
    Memo dslSelect(); // 쿼리 DSL 단일행 조회
    List<Memo> dslSelect2(); // 쿼리 DSL 다중행 조회
    List<Memo> dslSelect3(String searchType, String searchName);
    List<Memo> dslJoin(); // 쿼리 DSL 조인
    Page<MemberMemoDTO> dslJoinPaging(Criteria cri, Pageable pageable); // 조인 + 동적 쿼리 + 페이지네이션
}

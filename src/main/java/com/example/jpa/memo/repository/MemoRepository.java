package com.example.jpa.memo.repository;

import com.example.jpa.entity.MemberMemoDTO;
import com.example.jpa.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long>,  // <Entity 타입, ID 타입>
                                        MemoCustomRepository, // 커스텀 레퍼지토리 (새로 추가됨)
                                        QuerydslPredicateExecutor<Memo> { // 쿼리 DSL에서 제공되는 몇몇 함수들을 제공하는 인터페이스 (새로 추가됨)
    // JpaRepository로부터 추상 메소드 자동 상속 받음
    // 당분간은 서버를 실행시키지 않고 testcode로만 띄울 예정

    // testCode03 - 쿼리 메소드
    Memo findByWriterAndText(String writer, String text);
    List<Memo> findByMnoLessThan(Long mno); // 결과 값이 단일 행이 아니고 여러 행이라

    Memo findByMno(Long mno);
    List<Memo> findByMnoBetween(Long mno1, Long mno2);
    List<Memo> findByWriterContaining(String writer);
    Memo findByWriterOrderByWriterDesc(String writer);
    List<Memo> findByMnoIn(List<Long> mnos);

    // 쿼리 메소드의 마지막 매개변수에 Pageable을 주면 페이징 처리됨 - 다만 Page<>로 받아야 함
    Page<Memo> findByMnoLessThanEqual(Long mno, Pageable pageable);


    // JPQL
    // SQL과 비슷하나 엔터티를 사용해서 SQL문 작성함
    // select, update, delete는 제공되는데 insert는 제공되지 않음 XXXX
    // 1.테이블명이 아니라 엔터티명(=클래스명)이 사용됨 (중요**)
    // 2.속성(필드)는 대소문자를 구분함
    // 3.SQL 키워드는 대소문자 구분 XX
    // 4.별칭은 필수
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc(); // 메소드명은 자유로이

    // JPQL 파라미터 전달 @Param(변수명), 변수를 사용할 땐 :변수명 (콜론+변수명)
    @Query("select m from Memo m where m.mno > :num order by m.mno desc")
    List<Memo> getListDesc2(@Param("num") Long mno); // 여기 이 num을 위 Query구문 num에 전달

    // JPQL select문의 실행 결과를 컬럼 하나하나씩 선별적으로 받으려면 Object[] 사용함 (또는 DTO 객체)
    @Query("select m.writer, m.text from Memo m where m.mno > :num order by m.mno desc")
    List<Object[]> getListDesc3(@Param("num") Long mno);

    //JPQL 업데이트
    @Transactional // 트랜잭션에 반영한다는 뜻
    @Modifying // update나 delete 작업할 땐 이 2가지는 필수로 기재할 것
    @Query("update Memo m set m.writer = :writer where m.mno = :mno") // Memo 엔터티의 필드(m.writer 등)에 해당 매개변수(writer)를 담아냄
    int updateMemo(@Param("writer") String writer, @Param("mno") Long mno);

    // JPQL 업데이트 - 객체 파라미터를 넘기는 방법 #{객체}
    @Transactional
    @Modifying
//    @Query("update Memo m set m.writer = ?, m.text = ? where m.mno = ?") // ? 의 값은 죄다 (메모 객체) a 안에 들어 있음
    @Query("update Memo m set m.writer = :#{#a.writer}, m.text = :#{#a.text} where m.mno = :#{#a.mno}")
    int updateMemo(@Param("a") Memo memo); // 메모 객체가 a 라는 이름으로!

    @Transactional
    @Modifying
    @Query("delete from Memo m where m.mno = :mno")
    int deleteMemo(@Param("mno") Long mno);

    // JPQL 마지막 매개변수에 Pageable을 주면 자동으로 페이징 처리됨 - Page<>로 받아야 함
    // @Query("select m from Memo m where m.mno <= :a")
    // 혹시 수동으로 처리하고자 한다면?
    // page 처리에는 countQuery가 필요함 (countQuery구문은 직접 작성하는 게 가능함)
    @Query(value = "select m from Memo m where m.mno <= :a",
            countQuery = "select count(m) from Memo m where m.mno <= :a")
    Page<Memo> getListJPQL(@Param("a") Long mno, Pageable pageable);

    // select mno, writer, text, concat(writer, text) as col, current_timestamp
    // from memo
    // where mno <= 100;
    // 페이지 처리하는 JPQL 방식으로
    @Query(value = "select m.mno, m.writer, m.text, " +
                    "concat(m.writer, m.text) as col, current_timestamp " +
                    "from Memo m where m.mno <= :a")
    Page<Object[]> getListJPQL2(@Param("a") Long mno ,Pageable pageable);

    // 네이티브 쿼리 - JPQL이 너무 어려우면 SQL 방식으로 사용하는 것을 제공해줌
    @Query(value = "select * from memo where mno = ?", nativeQuery = true)
    Memo getNative(Long mno);

    // 구현체 만드는 구문은 인터페이스에서 이렇게 호출하는 것과 동일함
    // MemoCustomRepositoryImle 에서
    // public List<Memo> mtoJoin1(Long mno) - 이걸 만들지 않아도 된단 소리
    //@Query("select m from Memo m inner join m.member x where m.mno >= :a")
    //List<Memo> mtoJoin1(@Param("a") long a);

    @Query(value = "select new com.example.jpa.entity.MemberMemoDTO(x.id, x.name, x.signDate, m.mno, m.writer, m.text) " +
            "from Memo m left join m.member x where m.text like %:text%"
            ,countQuery = "select count(m) from Memo m left join m.member x where m.text like %:text%"
    )
    Page<MemberMemoDTO> joinPage(@Param("text") String text, Pageable pageable);

}

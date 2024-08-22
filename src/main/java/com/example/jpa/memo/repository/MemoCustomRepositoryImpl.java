package com.example.jpa.memo.repository;

import com.example.jpa.entity.*;
import com.example.jpa.util.Criteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;


public class MemoCustomRepositoryImpl implements MemoCustomRepository {
    // MemoRepository가 MemoCustomRepository를 상속 받음
    @PersistenceContext // 엔터티 매니저를 주입 받을 때 사용하는 어노테이션
    private EntityManager entityManager; // 영속성 컨텍스트에 기록해줌


    // build 폴더 생생해서 Q 뭐시기 파일 생성하고 레파지토리 가서 쿼리 DSL 상속 받고 구현체(여기)에 생성자 만들기
    /////////////////////// 쿼리 DSL 사용 시 엔터티매니저를 받아서 JPAFactory를 멤버변수에 저장시킴
    private JPAQueryFactory jpaQueryFactory;
    public MemoCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager); // 쿼리 DSL 팩토리는 생성될 때 엔터티매니저를 매개변수로 받음
    }

    @Override
    @Transactional
    public int updateTest(String writer, long mno) {
        // 기존의 인터페이스를 통해서 처리하던 JPQL구문을 직접 작성할 수 있게 됨 (자유도 UP)
        // JDBC랑 유사함
        String sql = "update Memo m set m.writer = :a where m.mno = :b"; // JPQL
        Query query = entityManager.createQuery(sql);
        query.setParameter("a", writer); // a 매개변수에 writer를 대입함
        query.setParameter("b", mno);

        int result = query.executeUpdate(); // insert, update, delete 구문 실행할 때 씀
        return result;
    }

    @Override
    public List<Memo> mtoJoin1(Long mno) {
        TypedQuery<Memo> result = entityManager.createQuery( // TypedQuery<Memo> - 반환 타입이 Memo로 확실할 때 적음, sql문 옆에 Memo.class까지 한 세트
                // 조인의 테이블 위치에 엔터티명이 들어감
                // on 조건절 따로 없음
                // 식별 관계일 때 한정
                "select m from Memo m inner join m.member where m.mno <= :mno", Memo.class); // m.member -> Memo.java안에 들어 있는 멤버 엔터티
                //"select m from Memo m left join m.member where m.mno <= :mno", Memo.class);
                // "select m from Memo m right join m.member where m.mno <= :mno", Memo.class);
        result.setParameter("mno", mno); // 파라미터 세팅
        List<Memo> list = result.getResultList(); // 리스트 타입으로 반환 받음
        return list;
    }

    @Override
    public List<Object[]> mtoJoin2(Long mno) {
        TypedQuery<Object[]> result =  entityManager.createQuery( // TypedQuery<Object[]> - 반환 타입이 Object로 확실할 때 적음, sql문 옆에 Object[].class까지 한 세트
                "select m, x from Memo m left join m.member x where m.mno <= :mno", Object[].class
        );

        result.setParameter("mno", mno);
        List<Object[]> list = result.getResultList();

        return list;
    }

    @Override
    public List<Object[]> mtoJoin3(String writer) {
        TypedQuery<Object[]> result = entityManager.createQuery(
                "select m, x from Memo m inner join Member x on m.writer = x.name where m.writer = :writer", Object[].class);
        // 달라지는 점
        // m.member x 가 아닌
        // Member x 라고 해당 엔터티명을 적음
        result.setParameter("writer", writer);
        List<Object[]> list = result.getResultList();
        return list;

    }

    @Override
    public List<Memo> mtoJoin4() {
        TypedQuery<Memo> result = entityManager.createQuery(
                "select m from Memo m left join fetch m.member x", Memo.class
        );
        // 파라미터(매개변수) 없으니까 setParameter 생략
        List<Memo> list = result.getResultList();
        return list;
    }

    @Override
    public Member otmJoin1(String id) {
        TypedQuery<Member> result = entityManager.createQuery(
                "select m from Member m inner join m.list where m.id = :id", Member.class
        );
        result.setParameter("id", id);
        // 1행인 경우에는 getResultList() 말고 getSingleResult()로
        Member m = result.getSingleResult();
        return m;
    }

    @Override
    public List<Member> otmJoin2(String id) {
        TypedQuery<Member> result = entityManager.createQuery(
                //"select m from Member m inner join fetch m.list x where m.id = :id", Member.class
                "select distinct m from Member m inner join fetch m.list x where m.id = :id", Member.class // OneToMany 조인은 distinct 필수
        );
        result.setParameter("id", id);
        List<Member> list = result.getResultList();
        return list;
    }

    @Override
    public List<MemberMemoDTO> otmJoin3(String id) {

        //구문의 select 절에는 생성자의 맵핑하는 구문이 들어감
        TypedQuery<MemberMemoDTO> result = entityManager.createQuery(
                "select new com.example.jpa.entity.MemberMemoDTO(m.id, m.name, m.signDate, x.mno, x.writer, x.text ) " +
                        " from Member m inner join m.list x where m.id = :id"
                ,MemberMemoDTO.class
        );

        result.setParameter("id", id);
        List<MemberMemoDTO> list = result.getResultList();

        return list;
    }

    // select * from memo m left join member x on m.member_id = x.id where m.writer like '%1%';
    // jpql로 사용하고, memberMemoDTO 타입으로 반환 받는 구문 작성하기
    @Override
    public List<MemberMemoDTO> otmJoin4(String text) { // Pageable 새로 추가함
        TypedQuery<MemberMemoDTO> result = entityManager.createQuery(
                "select new com.example.jpa.entity.MemberMemoDTO(x.id, x.name, x.signDate, m.mno, m.writer, m.text)" +
                        "from Memo m left join Member x on m.writer = x.id where m.writer like :writer"
                , MemberMemoDTO.class);
        result.setParameter("writer", "%" + text + "%"); // 파라미터 세팅
        List<MemberMemoDTO> list = result.getResultList();
        return list;
    }

//    @Override
//    public Page<MemberMemoDTO> joinPage(String text, Pageable pageable) {
//        TypedQuery<MemberMemoDTO> result = entityManager.createQuery(
//            "select new com.example.jpa.entity.MemberMemoDTO(x.id, x.name, x.signDate, m.mno, m.writer, m.text) " +
//                     "from Memo m left join m.member x where m.writer like :writer"
//                ,MemberMemoDTO.class
//        );
//
//        result.setParameter("writer", "%" + text + "%"); //파라미터 셋팅
//        result.setFirstResult(  (int)pageable.getOffset()  ); //페이지 시작 번호 - PageRequest.of(0, 10) <- 앞에값
//        result.setMaxResults( pageable.getPageSize() ); //데이터 개수 - PageRequest.of(0, 10) <- 뒤에값
//        List<MemberMemoDTO> list = result.getResultList(); //데이터
//
//        //countQuery를 실행
//        Query countQuery  = entityManager.createQuery(
//                "select count(m) from Memo m left join m.member x where m.writer like :writer"
//        );
//        countQuery.setParameter("writer", "%" + text + "%"); //파라미터 셋팅
//        Long count = (Long)countQuery.getSingleResult(); //한 행의 결과가 반환됨
//
//        //실행결과를 Pagealbe객체에 담는다.
//        PageImpl<MemberMemoDTO> page = new PageImpl<>(list, pageable, count); //데이터, 페이지객체, 토탈카운트
//
//        return page;
//    }


    /////////////////////////////////

    // 쿼리 DSL
    @Override
    public Memo dslSelect() {
        // 엔터티를 대신해서 Q 클래스 사용함
        QMemo memo = QMemo.memo; // Q클래스에 담긴 변수 사용하려면 Q타입에 담아주기

        // 다른 sql 키워드 쓰고 싶으면 아래 참고
        // http://querydsl.com/static/querydsl/3.6.3/reference/ko-KR/html_single/#d0e265

        Memo m = jpaQueryFactory.select(memo) // 엔터티 타입으로 받는 방식
                .from(memo)
                .where(memo.mno.eq(11L)) // IN
                .fetchOne(); // 단일 행 조회 시 fetchOne() : 엔터티 타입으로 반환시킴, 다중 행 조회 시 fetch() : List 타입으로 반환시킴
                             //insert, update, delete는 execute()

        // select 값을 선택적으로 받을 때는
        //Tuple t = jpaQueryFactory.select(memo.mno, memo.writer) // Tuple 타입으로 받는 방식
                //.from(memo)
                //.where(memo.mno.eq(11L)) // IN
                //.fetchOne();

        //System.out.println(t.get(memo.mno));
        //System.out.println(t.get(memo.writer));

        return m;
    }

    @Override
    public List<Memo> dslSelect2() {
        QMemo memo = QMemo.memo;

        List<Memo> list = jpaQueryFactory.select(memo)
                .from(memo)
                .where(memo.text.like("%2%"))
                .orderBy(memo.text.asc())
                .fetch(); // 다중 행이라 List 타입으로 반환시켜줌

        return list;
    }

    @Override
    public List<Memo> dslSelect3(String searchType, String searchName) {
        QMemo memo = QMemo.memo;

        // 동적 쿼리 구문을 만들 때는 BooleanBuilder 클래스를 사용함
        BooleanBuilder builder = new BooleanBuilder();
        if (searchType.equals("writer")) {
            builder.and(memo.writer.like("%"+searchName+"%")); // where 조건절을 붙일 수 있음
        }

        if (searchType.equals("text")) {
            builder.and(memo.text.like("%"+searchName+"%"));
        }

        List<Memo> list = jpaQueryFactory.select(memo)
                .from(memo)
                .where(builder) // BooleanBuilder implements Predicate 상속 받기 때문에
                .fetch();
        return List.of();
    }

    @Override
    public List<Memo> dslJoin() {
        QMemo memo = QMemo.memo;
        QMember member = QMember.member;

        List<Memo> list = jpaQueryFactory.select(memo)
                .from(memo)
                //.join(memo.member, member) // 엔터티 A.엔터티B = 엔터티B
                .leftJoin(memo.member, member)
                .fetch();
        return list;
    }

    @Override
    public Page<MemberMemoDTO> dslJoinPaging(Criteria cri, Pageable pageable) {

        QMemo memo = QMemo.memo;
        QMember member = QMember.member;

        // 1. BooleanBuilder
        BooleanBuilder builder = new BooleanBuilder();
        if(cri.getSearchType().equals("mno") && !cri.getSearchName().isEmpty()) { // searchType == "mno"  그리고 searchType != "" (빈 값)
            builder.and(memo.mno.eq(Long.parseLong(cri.getSearchName()))); // where mno = ?
        }

        if(cri.getSearchType().equals("text")) {
            builder.and(memo.text.like("%"+cri.getSearchName()+"%")); // where text like ?
        }

        if(cri.getSearchType().equals("writer")) {
            builder.and(memo.writer.like("%"+cri.getSearchName()+"%")); // where writer like ?
        }

        if(cri.getSearchType().equals("textWriter")) {
            builder.and(memo.text.like("%"+cri.getSearchName()+"%")); // wherer text like ? or writer like ?
            builder.or(memo.writer.like("%"+cri.getSearchName()+"%"));
        }

        // 2. Join 구문 실행 - 결과를 DTO로 받아야 함
//        "select new com.example.jpa.entity.MemberMemoDTO(x.id, x.name, x.signDate, m.mno, m.writer, m.text) " +
//                     "from Memo m left join m.member x where 동적 쿼리"
        List<MemberMemoDTO> list = jpaQueryFactory.select(
                Projections.constructor(MemberMemoDTO.class,
                        member.id, member.name, member.signDate, memo.mno, memo.writer, memo.text) // 타입, 생성자의 순서가 들어감
        ).from(memo)
                .leftJoin(memo.member, member) // 엔터티명.엔터티명 (=메모 안에 들어 있는 멤버와 그냥 멤버 연결)
                .where(builder)
                .orderBy(memo.mno.asc())
                .offset(pageable.getOffset()) // limit 0, 10 앞의 값 -> 0
                .limit(pageable.getPageSize()) // limit 0, 10 뒤의 값 -> 10
                .fetch();

        // 3. countQuery 실행 - 결과를 DTO로 받을 필요 없음
        long total = jpaQueryFactory.select(memo)
                .from(memo)
                .leftJoin(memo.member, member)
                .where(builder)
                .fetch().size(); // countQuery

        // 4. Page 객체에 맵핑 시키기
        PageImpl<MemberMemoDTO> page = new PageImpl<>(list, pageable, total); // 데이터, 페이지 객체, 전체 게시글 수

        return page;
    }
}

package com.example.jpa.memo.repository;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class MemoCustomRepositoryImpl implements MemoCustomRepository {
    // MemoRepository가 MemoCustomRepository를 상속 받음
    @PersistenceContext // 엔터티 매니저를 주입 받을 때 사용하는 어노테이션
    private EntityManager entityManager; // 영속성 컨텍스트에 기록해줌

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
}

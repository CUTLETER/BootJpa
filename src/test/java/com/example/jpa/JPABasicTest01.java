package com.example.jpa;

import com.example.jpa.entity.Memo;
import com.example.jpa.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class JPABasicTest01 {
    @Autowired
    MemoRepository memoRepository;

//    ORM 작업 - connection conn, preparedstatement pstmt 등 DB와 연결했던 작업이 자동으로 됨

//    JPA - 인터페이스 모음

//    하이버네이트 - JPA의 구현체, JDBC API 동작시킴

//    insert 작업
//    save(엔터티 객체)

//    select 작업
//    findById(키 타입),  findAll, getOne(키 타입)

//    update 작업
//    save(엔티티 객체)

//    delete 작업
//    deleteById(키 타입), delete(엔터티 객체)

    // insert - save
    @Test
    public void testCode01() {
        // memoRepository.save(Memo.builder().writer("test").text("test").build()); // 객체 생성

//        for (int i=0; i<=100; i++) {
//            memoRepository.save(
//                    Memo.builder().writer("example"+i)
//                            .text("sample"+i).build()
//            );
//        }
    }

    // select - find (단일 값 조회)
    @Test
    public void testCode02() {
        Optional<Memo> result = memoRepository.findById(50L); // 하나의 값을 조회 (예시-50번)

        if(result.isPresent()) { // 값이 있으면 true
            Memo memo = result.get();
            System.out.println(memo.toString());
        } else {
            System.out.println("조회할 값이 없습니다.");
        }
    }

    // select - all (다중 값 조회)
    @Test
    public void testCode03() {
        List<Memo> list = memoRepository.findAll();

        for (Memo memo : list) {
            System.out.println(memo.toString());
        }
    }

    // update - save
    @Test
    public void testCode04() {
        // 자체적으로 해당 값이 있는지 없는지 조회해서(select) 데이터가 없으면 insert 처리하고, 있으면 update 처리함
        Memo result = memoRepository.save(Memo.builder().mno(50L).writer("업데이트 완료").text("update finished").build());
        System.out.println("업데이트 된 엔터티 : "+result);
    }

    // delete - delete or deleteById
    @Test
    public void testCode05() {
        memoRepository.deleteById(50L);
    }
}

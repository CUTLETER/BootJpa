package com.example.jpa;

import com.example.jpa.entity.Member;
import com.example.jpa.member.repository.MemberRepository;
import com.example.jpa.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JPACustomTest05 {
    @Autowired
    MemoRepository memoRepository;

    @Autowired
    MemberRepository memberRepository;

//    @Test
//    public void testCode01() {
//        int result = memoRepository.updateTest("구현체를 통한 업데이트", 6L);
//        System.out.println("성공? 실패? "+result);
//    }

    // Member테이블의 예시 데이터 삽입
//    @Test
//    public void testCode02() {
//        for (int i = 1; i <= 5; i++) {
//            memberRepository.save(Member.builder().id("abc" + i).name("admin" + i).build());
//            // sign_date 는 JPA 대신해서 데이터를 삽입함
//        };
//    }
}

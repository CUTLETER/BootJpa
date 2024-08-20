package com.example.jpa;

import com.example.jpa.entity.Memo;
import com.example.jpa.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class JPAQueryMethodTest03 {
    // 쿼리 메소드 JPA 인터페이스의 메소드 모형을 보고, SQL을 대신 실행시킴 (다양한 SELECT 구문을 활용함)
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html


    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testCode01() {
//        먼저 MemoRepository 인터페이스 가서 추상 메소드 추가해야 함
        Memo memo = memoRepository.findByWriterAndText("example10", "samle10");
        System.out.println(memo);

        List<Memo> list = memoRepository.findByMnoLessThan(20L);
        System.out.println(list.toString());
    }

    @Test
    public void testCode02() {
        Memo memo = memoRepository.findByMno(11L);
        System.out.println(memo);
    }

    @Test
    public void testCode03() {
        List<Memo> list = memoRepository.findByMnoBetween(10L,20L);
        System.out.println(list.toString());
    }

    @Test
    public void testCode04() {
        List<Memo> list = memoRepository.findByWriterContaining("10");
        System.out.println(list.toString());
    }

    @Test
    public void testCode05() {
        Memo memo = memoRepository.findByWriterOrderByWriterDesc("example1");
        System.out.println(memo);
    }

    @Test
    public void testCode06() {
        List<Long> mnos = new ArrayList<>();
        mnos.addAll(Arrays.asList(10L,20L,30L,40L,50L));
        List<Memo> list = memoRepository.findByMnoIn(mnos);
        System.out.println(list.toString());
    }

    @Test
    public void testCode07() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoLessThanEqual(50L, pageable);

        for(Memo memo : result.getContent()) {
            System.out.println(memo.toString());
        }
    }
}

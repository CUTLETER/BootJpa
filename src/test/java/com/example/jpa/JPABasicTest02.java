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

import java.util.List;

@SpringBootTest
public class JPABasicTest02 {
    @Autowired
    MemoRepository memoRepository;

    // 기본 정렬
    @Test
    public void testCode01() {
//        Sort.by("mno").ascending(); - order by mno desc;
//        Sort sort = Sort.by("mno").descending(); - order by mno asc;

//      만약 두 가지 기준으로 정렬하고 싶다면? order by writer desc, text asc;
        Sort sort1 = Sort.by("writer").descending();
        Sort sort2 = Sort.by("text").ascending();
        Sort sort3 = sort1.and(sort2); // 정렬 조건의 결합은 and()로
        List<Memo> list = memoRepository.findAll(sort3);
        System.out.println(list.toString());
    }

    @Test
    public void testCode02() {

    //    Pageable pageable = PageRequest.of(0, 10); // of(페이지 번호), count(데이터 개수)
    //    Pageable pageable = PageRequest.of(2, 10); // 2페이지의 10개의 데이터
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> page = memoRepository.findAll(pageable);

        // 페이지 타입 결과 안에는 데이터, 페이지에 대한 정보가 들어감
        System.out.println("page에 담긴 정보 : "+page);
        System.out.println("전체 데이터 수 : "+page.getTotalElements());
        System.out.println("조회된 데이터 : "+page.getContent());
        System.out.println("전체 페이지 수 : "+page.getTotalPages());
        System.out.println("현재 페이지 : "+page.getNumber());
        System.out.println("페이지 크기(한 페이지에 몇 개의 항목이 있는지) : "+page.getSize());
        System.out.println("이전 페이지 여부 : "+page.hasPrevious());
        System.out.println("다음 페이지 여부 : "+page.hasNext());
        System.out.println("현재 페이지가 맨뒤인지 확인 : "+page.isLast());
        System.out.println("현재 페이지가 맨앞인지 확인 : "+page.isFirst());
    }

}

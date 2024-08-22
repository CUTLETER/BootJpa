package com.example.jpa;

import com.example.jpa.entity.MemberMemoDTO;
import com.example.jpa.entity.Memo;
import com.example.jpa.memo.repository.MemoRepository;
import com.example.jpa.util.Criteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class JPAQueryDslTest06 {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testCode01() {
        Memo m = memoRepository.dslSelect();
        //System.out.println(m.toString());
    }

    @Test
    public void testCode02() {
        List<Memo> list = memoRepository.dslSelect2();
        for (Memo m : list) {
            System.out.println(m.toString());
        }
    }

    @Test
    public void testCode03() {
        String searchType = "text"; // "writer";
        String searchName = "2";
        List<Memo> list = memoRepository.dslSelect3(searchType, searchName);
        for (Memo m : list) {
            System.out.println(m.toString());
        }
    }

    @Test
    public void testCode04() {
        List<Memo> list = memoRepository.dslJoin();
        for (Memo m : list) {
            System.out.println(m.toString());
        }
    }

    @Test
    public void testCode05() {
        Criteria cri = new Criteria(1, 10);
        cri.setSearchType("writer"); // 조회하는 타입
        cri.setSearchName("1");

        Pageable pageable = PageRequest.of(cri.getPage()-1, cri.getAmount());

        Page<MemberMemoDTO> list = memoRepository.dslJoinPaging(cri, pageable);
        for (MemberMemoDTO m : list.getContent()) {
            System.out.println(m);
        }
    }

}

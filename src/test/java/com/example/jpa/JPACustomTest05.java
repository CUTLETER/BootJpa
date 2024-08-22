package com.example.jpa;

import com.example.jpa.entity.Member;
import com.example.jpa.entity.MemberMemoDTO;
import com.example.jpa.entity.Memo;
import com.example.jpa.member.repository.MemberRepository;
import com.example.jpa.memo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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

    // 조인 테스트1
    @Test
    public void testCode03() { // left 조인 예시
        List<Memo> list = memoRepository.mtoJoin1(10L);
        for (Memo memo : list) {
            System.out.println(memo.toString());
        }
    }

    // 두 개 이상의 엔터티를 동시에 조회하기
    @Test
    public void testCode04() {
        List<Object[]> list = memoRepository.mtoJoin2(10L);

        for (Object[] obj : list) {
            System.out.println(Arrays.toString(obj));
        }
    }

    @Test
    public void testCode05() {
        List<Object[]> list = memoRepository.mtoJoin3("admin1");
        for (Object[] obj : list) {
            System.out.println(Arrays.toString(obj));
        }
    }

    // Eager 조인 VS Lazy 조인
    // 즉시 로딩 (EAGER) - 조회하자마자 데이터 한방에 불러옴
    // 지연 로딩 (LAZY) - 조회 시 필요한 시점(호출될 때마다)에 데이터를 불러옴
    @Test
    @Transactional // Lazy 방식을 쓰려면 이것도 달아놔야 함, 없으면 LazyInitializationException - no session 에러남
    public void testCode06() { // inner join 예시
        List<Memo> list = memoRepository.mtoJoin1(10L);
        //for (Memo memo : list) {
        //    System.out.println(memo.toString());
        //}

        // 먼저, 위에 향상된 for문 주석 처리하고 보면 됨
        // Eager 방식일 땐 select만 해도 데이터 한꺼번에 나오는 반면에 (결과 - select문 6개)
        // Lazy 방식일 땐 출력 요청이 담긴 향상된 for문이 주석 처리 되었기 때문에
        // 먼저 최소한의 조회만 하고 (결과 - select문 1개)
        // 향상된 for문 주석 처리 해지했을 때야(출력 호출 시) 데이터 불러옴 (결과 - select문 6개)

        Memo memo = list.get(0); // 실제 데이터를 조회할 때 뒤늦게 select가 동작함
        System.out.println(memo);
    }

    // 성능 향상 Fetch 조인
    // select문 6개였던 Lazy, Eager와는 다르게 조회와 출력 합쳐서 select문 단 1개
    // 다만 페이징 API 사용 불가
    @Test
    public void testCode07() {
        List<Memo> list = memoRepository.mtoJoin4();
        for (Memo memo : list) {
            System.out.println(memo.toString()); // 조회와 출력 합쳐서 select문 단 1개
        }
    }

    //////////////////////

    // One To Many 일반 방식
    @Test
    public void testCode08() {
        Member m = memoRepository.otmJoin1("abc1");
        System.out.println(m.toString());
    }

    // One To Many Fetch 방식
    @Test
    public void testCode09() {
        List<Member> list = memoRepository.otmJoin2("abc1");
        for (Member mem : list) {
            System.out.println(mem.toString());
        }
    }


    // DTO로 반환 받기
    @Test
    public void testCode10() {
        List<MemberMemoDTO> list = memoRepository.otmJoin3("abc1");
        for (MemberMemoDTO m : list){
            System.out.println(m.toString());
        }
}

    @Test
    public void testCode11() {
           List<MemberMemoDTO> list = memoRepository.otmJoin4("1");
           for (MemberMemoDTO mm : list) {
               System.out.println(mm.toString());
           }
        }

    @Test
    public void testCode12() {
        Page<MemberMemoDTO> list =
                memoRepository.joinPage("1", PageRequest.of(0, 10) );

        for( MemberMemoDTO m : list.getContent()) {
            System.out.println(m.toString()); //데이터
        }

        System.out.println( "전체 게시글 수:" +  list.getTotalElements()  );




    }


}

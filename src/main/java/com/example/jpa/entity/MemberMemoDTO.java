package com.example.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberMemoDTO {

    // 멤버, 메모 엔터티의 필요한 값을 명시함
    private String id; // 멤버 꺼
    private String name; // 멤버 꺼
    private LocalDate signDate; // 멤버 꺼
    private long mno; // 메모 꺼
    private String writer; // 메모 꺼
    private String text; // 메모 꺼
}

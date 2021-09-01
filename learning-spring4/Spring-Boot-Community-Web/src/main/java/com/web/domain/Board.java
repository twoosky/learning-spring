package com.web.domain;

import com.web.domain.enums.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Board implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본키 자동으로 할당, 키 생성을 데이터베이스에 위임하는 IDENTITY 전략
    private Long idx;

    @Column
    private String title;

    @Column
    private String subTitle;

    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    // enum 타입 매핑용 어노테이션
    // 실제로 자바 enum 형이지만 데이터베이스의 String형으로 변환하여 저장하겠다고 선언
    private BoardType boardType;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @OneToOne(fetch = FetchType.LAZY)
    // Board와 User를 1:1 관계로 설정
    // 실제로 DB에 저장될 때는 User 객체가 저장되는 것이 아니라 User 의 PK인 user_idx 값이 저장됩니다.
    //fetch 는 eager 와 lazy 두 종류가 있는데
    //eager는 처음 Board 도메인을 조회할 때 즉시 관련 User 객체를 함께 조회한다는 뜻이고.
    //lazy는 User 객체를 조회하는 시점이 아닌 객체가 실제 사용될 때 조회한다는 뜻입니다.
    private User user;

    @Builder
    public Board(String title, String subTitle, String content, BoardType boardType, LocalDateTime createdDate, LocalDateTime updatedDate, User user) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.user = user;
    }

}

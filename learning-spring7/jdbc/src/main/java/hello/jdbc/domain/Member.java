package hello.jdbc.domain;

import lombok.Data;

// lombok의 @Data 를 사용하면
// 1. toString()을 오버라이딩 해준다.
// - 객체 자체를 출력해도 객체 내부 값들이 예쁘게 출력됨
// 2. equals()와 hashcode 를 오버라이딩해준다.
// - 객체 내 모든 필드에 대한 equals()를 오버라이딩 한다.
// - 객체 내부 값이 모두 같으면 같은 객체라 판단
@Data
public class Member {

    private String memberId;
    private int money;

    public Member() {

    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}

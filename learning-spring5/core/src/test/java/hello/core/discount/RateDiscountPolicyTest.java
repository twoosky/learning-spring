package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// [comment] assertj.core.api import 해야됨 맨날 헷갈림 기억 좀
// [comment] mac에서 option+enter 단축키로 static import 하면 Assertions. 생략 가능
import static org.assertj.core.api.Assertions.*;


class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")  // Junit5부터 @DisplayName 지원
    void vip_o() {
        // given
        Member member = new Member(1L, "memberVIP", Grade.VIP);
        // when
        int discount = discountPolicy.disCount(member, 10000);
        // then

        // Assertions.assertThat(discount).isEqualTo(1000);
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_x() {
        // given
        Member member = new Member(1L, "memberBasic", Grade.BASIC);
        // when
        int discount = discountPolicy.disCount(member, 10000);
        // then
        assertThat(discount).isEqualTo(0);
    }
}
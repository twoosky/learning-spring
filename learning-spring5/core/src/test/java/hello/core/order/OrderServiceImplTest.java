package hello.core.order;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderServiceImplTest {

    @Test
    @DisplayName("생성자 주입을 사용한 테스트")
    void createOrder() {
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "sky", Grade.BASIC));

        OrderServiceImpl orderService = new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);

        Assertions.assertThat(order.getMemberId()).isEqualTo(1L);
        Assertions.assertThat(order.getItemName()).isEqualTo("itemA");
        Assertions.assertThat(order.getItemPrice()).isEqualTo(10000);
    }
}

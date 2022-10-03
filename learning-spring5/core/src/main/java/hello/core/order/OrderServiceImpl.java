package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicyImpl;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

public class OrderServiceImpl implements OrderService {

    private final MemberService memberService = new MemberServiceImpl();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicyImpl();

    // 단일 책임 원칙을 잘 지킨 예시
    // : 주문과 할인에 대한 책임을 분리시킴
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberService.findMember(memberId);
        int disCountPrice = discountPolicy.disCount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, disCountPrice);
    }
}

package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    // [comment] final이 붙은 경우 변수 선언 시 할당하거나 생성자로 할당해줘야됨.
    // [comment] DIP 준수: OrderServiceImpl은 인터페이스에만 의존한다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // [comment] 생성자 주입
    // - AppConfig에서 애플리케이션의 실제 동작에 필요한 구현 객체를 생성한다.
    // - AppConfig는 생성한 객체 인스턴스의 참조(레퍼런스)를 생성자를 통해서 주입(연결) 해준다.
    // - OCP 준수: 구현 객체를 변경하는 경우 OrderServiceImpl의 코드는 변경하지 않아도 된다.
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }


    /* 수정자 주입
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
     */

    /*
     [comment] 아래 코드의 문제점
     - DIP 위반: 클라이언트인 OrderServiceImpl은 DiscountPolicy 인터페이스 뿐만 아니라,
       구체 클래스(FixDiscountPolicy, RateDiscountPolicy)도 함께 의존하고 있다.
     - OCP 위반: 할인 정책을 변경하려면 클라이언트인 OrderServiceImpl 코드를 고쳐야 한다.

    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

     [comment] 해결 방안
     - 인터페이스에만 의존하도록 코드를 수정하고, 외부로부터 DiscountPolicy 구현 객체를 주입 받아야 한다.
     */

    // 단일 책임 원칙을 잘 지킨 예시
    // : 주문과 할인에 대한 책임을 분리시킴
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int disCountPrice = discountPolicy.disCount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, disCountPrice);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}

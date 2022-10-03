package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class FixDiscountPolicyImpl implements DiscountPolicy {

    private int disCountFixAmount = 1000;

    @Override
    public int disCount(Member member, int price) {
        // Enum type 비교는 == 사용
        if (member.getGrade() == Grade.VIP) {
            return disCountFixAmount;
        }
        return 0;
    }
}

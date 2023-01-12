package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService {

    // 아래의 경우 의존 관계가 인터페이스 뿐만 아니라 구현체까지 모두 의존하는 문제점이 있다. -> DIP 위반
    private final MemberRepository memberRepository;

    // [comment] 의존관계를 자동으로 주입해주는 어노테이션
    // - applicationContext.getBean(MemberRepository.class) 와 같은 의미
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}

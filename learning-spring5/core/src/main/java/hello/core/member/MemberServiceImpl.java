package hello.core.member;

// 구현체가 하나만 있는 경우 interface명 뒤에 Impl을 붙여 많이 쓴다.
public class MemberServiceImpl implements MemberService {

    // 아래의 경우 의존 관계가 인터페이스 뿐만 아니라 구현체까지 모두 의존하는 문제점이 있다. -> DIP 위반
    private final MemberRepository memberRepository;

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
}

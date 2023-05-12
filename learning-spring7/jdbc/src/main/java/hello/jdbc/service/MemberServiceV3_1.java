package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    // TransactionManager 구현체는 외부로부터 주입받아 사용
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    /**
     * 계좌이체
     * @param fromId: 이체하는 사람
     * @param toId: 이체받은 사람
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // 비즈니스 로직
            bizLogic(fromId, toId, money);
            // 성공시 트랜잭션 커밋
            transactionManager.commit(status);
        } catch (Exception e) {
            // 실패 시 트랜잭션 롤백
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
    }

    // 커넥션을 파라미터로 받아 트랜잭션에서 하나의 커넥션으로 사용
    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException(("이체 중 예외 발생"));
        }
    }
}

package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    /**
     * 계좌이체
     * @param fromId: 이체하는 사람
     * @param toId: 이체받은 사람
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con =  dataSource.getConnection();
        try {
            /**
             * 트랜잭션 시작
             * - default는 autocommit true이다. ture인 경우 쿼리 단위로 커밋되어 DB에 즉각 반영됨.
             * - 즉, 로직 중간 exception이 발생해도 이전 쿼리들은 롤백이 안된다.
             * - 따라서, autocommit false로 설정해야 트랜잭션을 시작할 수 있다.
             */
            con.setAutoCommit(false);

            // 비즈니스 로직
            bizLogic(con, fromId, toId, money);

            // 성공시 트랜잭션 커밋
            con.commit();
        } catch (Exception e) {
            // 실패 시 트랜잭션 롤백
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }
    }

    // 커넥션을 파라미터로 받아 트랜잭션에서 하나의 커넥션으로 사용
    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException(("이체 중 예외 발생"));
        }
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);  // autocommit을 true로 변경하지 않으면 커넥션 풀에 수동커밋 모드로 반환됨. 문제 발생할 수 있음
                con.close();  // 커넥션 풀을 사용하면 close()시 커넥션이 종료되는 것이 아니라, 풀에 반납된다.
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }
}

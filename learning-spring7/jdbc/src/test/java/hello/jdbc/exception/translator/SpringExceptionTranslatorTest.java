package hello.jdbc.exception.translator;

import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class SpringExceptionTranslatorTest {

    DataSource dataSource;
    Repository repository;

    @BeforeEach
    void init() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
    }

    @Test
    void sqlExceptionErrorCode() {
        String sql = "select bad grammer";

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch (SQLException e) {
            Assertions.assertThat(e.getErrorCode()).isEqualTo(42122);

            SQLErrorCodeSQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            DataAccessException resultEx = exTranslator.translate("insert", sql, e);
            log.info("resultEx", resultEx);
            Assertions.assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }

    @Test
    void exceptionTranslatorByDuplicateKey() {
        repository.create(new Member("myId", 1000));
        repository.create(new Member("myId", 1000));
    }

    @RequiredArgsConstructor
    static class Repository {
        private final DataSource dataSource;

        public void create(Member member) {
            String sql = "insert into member(member_id, money) values(?, ?)";

            try {
                Connection con = dataSource.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, member.getMemberId());
                stmt.setInt(2, member.getMoney());
                stmt.executeUpdate();
            } catch (SQLException e) {
                SQLErrorCodeSQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
                DataAccessException resultEx = exTranslator.translate("insert", sql, e);
                log.info("resultEx", resultEx);
                Assertions.assertThat(resultEx.getClass()).isEqualTo(DuplicateKeyException.class);
            }
        }
    }
}

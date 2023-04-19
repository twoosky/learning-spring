# Spring Framework
[스프링 DB 1편 - 데이터 접근 핵심 원리](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-1/dashboard) 정리 문서

# JDBC
**JDBC 등장 배경**  
* 애플리케이션 서버와 DB 연결 과정
<img src="https://user-images.githubusercontent.com/50009240/232737044-7d2f9469-465e-4204-8024-b19e39fd1424.png" width="400" height="100">

1. 커넥션 연결: 주로 TCP/IP를 사용해 애플리케이션 서버와 DB서버가 연결된다.
2. SQL 전달: 애플리케이션 서버는 SQL을 연결된 커넥션을 통해 DB에 전달한다.
3. 결과 응답: DB는 전달된 SQL을 수행하고 그 결과를 응답한다.

문제점
* DB 종류에 따라 DB 연결 방법이 모두 다르다.
* 만약, MySQL에서 Oracle DB로 변경한다면, 애플리케이션의 DB 사용 코드를 모두 수정해야 한다.
* 이러한 문제를 해결하기 위해 `JDBC`라는 자바 표준이 등장했다.

**JDBC란**
* JDBC는 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API이다.
* JDBC는 데이터베이스에서 자료를 쿼리하거나 업데이트하는 방법을 제공한다.
* JDBC 표준 인터페이스를 사용하면 위에서 언급한 문제들을 해결할 수 있다.
<img src="https://user-images.githubusercontent.com/50009240/232740119-8ccc8365-2f0b-40a7-8734-81c9a45175fe.png" width="500" height="230">

* java.sql.Connection: 연결
* java.sql.Statement: SQL을 담은 내용
* java.sql.ResultSet: SQL 요청 응답

**JDBC 드라이버란**
* JDBC 드라이버란 각각의 DB 벤더(회사)에서 자신의 DB에 맞도록 JDBC 인터페이스를 구현해 제공하는 라이브러리이다.
* 따라서 JDBC 드라이브만 교체하면 DB 종류를 변경할 수 있다.
* 개발자는 JDBC 표준 인터페이스 사용법만 학습하더라도, 다양한 DB에 동일하게 적용할 수 있게 된다.

# 커넥션 풀
**DB 커넥션 과정**

<img src="https://user-images.githubusercontent.com/50009240/232749626-7b3d040e-732b-4021-b2ed-68b467711287.png" width="600" height="180">

1. 애플리케이션 로직은 DB 드라이버를 통해 커넥션을 조회한다.
2. DB 드라이버는 DB와 TCP/IP 커넥션을 연결한다.
3. DB 드라이버는 연결이 완료되면, ID, PW와 기타 부가정보를 DB에 전달한다.
4. DB는 ID, PW를 통해 인증을 완료하고, 내부에 DB 세션을 생성한다.
5. DB는 커넥션이 생성되었다는 응답을 보낸다.
6. DB 드라이버는 커넥션 객체를 생성해 애플리케이션에 반환한다.

**커넥션 풀**
* 커넥션 풀이란 커넥션을 미리 생성해두고, 사용하는 방법이다.
* 데이터베이스 커넥션을 새로 생성하는 것은 과정도 복잡하고, 많은 시간이 소요되므로 커넥션 풀 사용한다.

**커넥션 풀 사용 과정**
1. 커넥션 풀 초기화
* 애플리케이션을 시작하는 시점에 필요한 만큼 커넥션을 미리 확보해서 커넥션 풀에 보관한다. 
* 커넥션 풀에 들어 있는 커넥션은 TCP/IP로 DB와 커넥션이 연결되어 있는 상태이기 때문에, 언제든지 SQL을 DB에 전달할 수 있다.
<img src="https://user-images.githubusercontent.com/50009240/233094582-eadb847b-3baa-48c7-a95a-0d8bd9761e7a.png" width="550" height="260">

2. 커넥션 풀 사용
* 애플리케이션 로직에서는 커넥션 풀을 통해 이미 생성되어 있는 커넥션을 객체 참조로 가져다 쓰면 된다.
* 커넥션 풀은 자신이 가지고 있는 커넥션 중 하나를 반환한다.
* 애플리케이션은 커넥션 풀에서 받은 커넥션을 사용해 SQL을 데이터베이스에 전달하고, 그 결과를 받아 처리한다.
<img src="https://user-images.githubusercontent.com/50009240/233094661-fb1dd214-7de7-4907-870b-c8171704f4bf.png" width="550" height="250">

3. 커넥션 반환
* 커넥션을 모두 사용하고 나면, 커넥션을 종료하는 것이 아니라, 다음에 다시 사용할 수 있도록 커넥션 풀에 반환한다.

**커넥션 풀 이점**
* 커넥션 풀을 통해 커넥션 생성 시간을 단축함으로써 사용자에게 빠른 응답을 반환할 수 있다.
* 커넥션 풀은 서버당 최대 커넥션 수를 제한할 수 있다. 따라서 DB에 무한정 연결이 생성되는 것을 막아주어 DB 보호하는 효과도 있다.

> * 커넥션 풀 오픈소스로는 `hikariCP`를 사용한다.
> * 스프링 부트 2.0부터는 기본 커넥션 풀로 hikariCP를 사용한다.

# DataSource
* DataSource 는 `커넥션을 획득하는 방법을 추상화` 하는 인터페이스 (자바에서 제공)
* 커넥션을 얻는 방법은 JDBC DriverManager를 직접 사용하거나, HikariCP 커넥션 풀을 사용하는 등 다양한 방법이 있다.
* 따라서, DataSource로 커넥션을 얻는 방법을 추상화하여 애플리케이션 로직은 변경하지 않도록 처리하는 것이다.
<img src="https://user-images.githubusercontent.com/50009240/233094978-3204359f-2321-4055-9106-38400dd57bb5.png" width="540" height="260">

> DriverManager는 DataSource 인터페이스를 사용하지 않지만, 스프링에서는 DataSource를 구현한 `DriverManagerDataSource` 클래스 제공

**DriverManagerDataSource 예시**
* DriverManager는 커넥션을 획득할 때마다 URL, USERNAME, PASSWORD 파라미터를 매번 전달해야 한다.
* DataSource를 사용하는 방식은 처음 객체를 생성할 때만 필요한 파라미터를 전달하고, 커넥션을 획득할 때는 단순히 getConnection()만 호출하면 된다.
```java
@Slf4j
public class ConnectionTest {

    // 1. DriverMaganer 사용 테스트
    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }

    // 2. DataSource인 DriverManagerDataSource 사용 테스트
    @Test
    void dataSourceDriverManager() throws SQLException {
        // DriverManagerDataSource - 항상 새로운 커넥션을 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();

        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }
}
```

**HikariCP 예시**
* HikariDataSource는 HikariCP 커넥션 풀을 사용하기 위한 DataSource 인터페이스의 구현체이다.
* 커넥션 풀에서 커넥션을 생성하는 작업은 애플리케이션 실행 속도에 영향을 주지 않기 위해 `별도의 쓰레드에서 작동한다.`
```java
@Test
void dataSourceConnectionPool() throws SQLException, InterruptedException {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setMaximumPoolSize(10);
    dataSource.setPoolName("MyPool");

    useDataSource(dataSource);
    Thread.sleep(1000);
}
```
> 별도의 쓰레드에서 커넥션을 생성하므로, Thread.sleep을 사용해야 커넥션 풀에 커넥션이 생성되는 로그를 확인할 수 있다. 

<details>
<summary>실행 결과</summary>
<div markdown="1">

```bash
#커넥션 풀 초기화 정보 출력
HikariConfig - MyPool - configuration:
HikariConfig - maximumPoolSize................................10
HikariConfig - poolName................................"MyPool"

#커넥션 풀 전용 쓰레드가 커넥션 풀에 커넥션을 10개 채움
[MyPool connection adder] MyPool - Added connection conn0: url=jdbc:h2:..user=SA
[MyPool connection adder] MyPool - Added connection conn1: url=jdbc:h2:..user=SA
[MyPool connection adder] MyPool - Added connection conn2: url=jdbc:h2:..user=SA
[MyPool connection adder] MyPool - Added connection conn3: url=jdbc:h2:..user=SA
[MyPool connection adder] MyPool - Added connection conn4: url=jdbc:h2:..user=SA
...
[MyPool connection adder] MyPool - Added connection conn9: url=jdbc:h2:..user=SA

#커넥션 풀에서 커넥션 획득1
[Test worker] INFO hello.jdbc.connection.ConnectionTest -- connection=HikariProxyConnection@1644236636 wrapping conn0: 
url=jdbc:h2:tcp://localhost/~/test user=SA, class=class com.zaxxer.hikari.pool.HikariProxyConnection

#커넥션 풀에서 커넥션 획득2
[Test worker] INFO hello.jdbc.connection.ConnectionTest -- connection=HikariProxyConnection@1824872646 wrapping conn1: 
url=jdbc:h2:tcp://localhost/~/test user=SA, class=class com.zaxxer.hikari.pool.HikariProxyConnection

MyPool - After adding stats (total=10, active=2, idle=8, waiting=0)
```
* HikariConfig: HikariCP 관련 설정을 확인할 수 있다. 풀의 이름(MyPool)과 최대 풀 수 (10) ..
* 별도의 쓰레드를 사용해 커넥션 풀에 커넥션을 채우고 있다.
* 마지막 로그를 보면 사용중인 커넥션 active=2, 풀에서 대기 상태인 커넥션 idle=8을 확인할 수 있다.

</div>
</details>


# 트랜잭션
* 데이터베이스에 데이터를 저장하는 가장 큰 이유는 트랜잭션을 지원하기 때문이다.
* `커밋(Commit)`: 모든 작업이 성공해서 데이터베이스에 정상 반영하는듯
* `롤백(Rollback)`: 작업 중 하나라도 실패해서 작업 이전으로 되돌리는 것

**트랜잭션 ACID**
* `원자성`: 트랜잭션 내에서 실행한 작업들은 마치 하나의 작업인 것처럼 모두 성공하거나, 모두 실패해야 한다.
* `일관성`: 모든 트랜잭션은 일관성 있는 데이터베이스 상태를 유지해야 한다. (무결성 제약 조건 항상 만족, ..)
* `격리성`: 동시에 실행되는 트랜잭션들이 서로에게 영향을 미치지 않도록 격리해야 한다.
* `지속성`: 트랜잭션을 성공적으로 끝내면 그 결과가 항상 기록되어야 하며, 문제가 발생하면 기록을 통해 복구가능해야 한다.

**트랜잭션 격리 수준**
* 격리성을 위한 순차 처리시 성능 이슈로 인해 동시 처리를 위한 트랜잭션 격리 수준을 4단계로 나눠 정의했다.
* `READ UNCOMMITED`: 커밋되지 않은 읽기
* `READ COMMITED`: 커밋된 읽기 (주로 사용)
* `REPEATABLE READ`: 반복 가능한 읽기
* `SERIALIZABLE`: 직렬화 가능

**데이터베이스 연결 구조와 DB 세션**
* 사용자는 WAS나 DB 접근 툴 같은 클라이언트를 사용해 DB 서버에 접근하여 연결을 요청하고 커넥션을 맺는다.
* 이때, DB 서버는 내부에 `세션`을 만들고, 세션을 통해 해당 커넥션으로 들어오는 모든 요청을 실행한다.
* 즉, 클라이언트에서 SQL을 전달하면, 현재 커넥션에 연결된 세션이 SQL을 실행한다.
* 세션은 트랜잭션을 시작하고, 커밋 또는 롤백을 통해 트랜잭션을 종료할 수 있으며 이후에 새로운 트랜잭션을 다시 시작할 수도 있다.

**트랜잭션 커밋과 롤백**
* 데이터 변경 쿼리를 실행하고, 데이터베이스에 그 결과를 반영하려면 커밋 명령어인 `commit`을 호출하고,
* 결과를 반영하고 싶지 않으면 롤백 명령어인 `rollback`을 호출하면 된다.
* **커밋을 호출하기 전까지는 임시로 데이터를 저장**하는 것이다.
* 따라서, 해당 트랜잭션을 시작한 세션(사용자)에게만 변경 데이터가 보이고, 다른 세션(사용자)에게는 변경 데이터가 보이지 않는다.





























package hello.core.singleton.state;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class StateFulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StateFulService stateFulService1 = ac.getBean(StateFulService.class);
        StateFulService stateFulService2 = ac.getBean(StateFulService.class);

        // ThreadA: A 사용자가 10000원 주문
        stateFulService1.order("userA", 10000);
        // ThreadB: B 사용자가 20000원 주문
        stateFulService2.order("userA", 20000);

        int price = stateFulService1.getPrice();
        System.out.println("price = " + price);

        // A 사용자의 주문 금액이 20000으로 나온다. -> 싱글톤 패턴 상태 유지 설계 시 문제점
        Assertions.assertThat(stateFulService1.getPrice()).isEqualTo(20000);
    }

    // [comment] TestConfig 클래스에는 왜 @Configuration 어노테이션을 안붙여줘도 될까?
    // - 위에서 ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class); 을 통해
    //   스프링 컨테이너에 TestConfig 클래스를 직접 스프링 빈으로 등록했기 때문이다.
    // - 직접 등록하지 않는 경우 구성 정보 클래스에 @Configuration 을 붙여야 한다.
    //   스프링 컨테이너는 @Configuration이 붙은 클래스를 찾아 스프링 빈으로 등록한다.
    static class TestConfig {
        @Bean
        public StateFulService stateFulService() {
            return new StateFulService();
        }
    }
}

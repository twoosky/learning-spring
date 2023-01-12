package hello.core.singleton.state;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class StateLessServiceTest {

    @Test
    void stateLessServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StateLessService stateLessService1 = ac.getBean(StateLessService.class);
        StateLessService stateLessService2 = ac.getBean(StateLessService.class);

        int userAPrice = stateLessService1.order("userA", 10000);
        int userBPrice = stateLessService2.order("userB", 20000);

        Assertions.assertThat(userAPrice).isEqualTo(10000);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public StateLessService stateLessService() {
            return new StateLessService();
        }
    }
}

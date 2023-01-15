package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    // [comment] ApplicationContext 인터페이스는 close 메서드를 지원하지 않는다.
    // - ConfigurableApplicationContext 구현 객체를 사용해야 한다.

    // 1. 인터페이스를 통한 콜백 메서드 호출
    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClientSpring client = ac.getBean(NetworkClientSpring.class);
        ac.close();
    }

    // 2. 설정 정보에 의한 콜백 메서드 호출
    @Test
    public void lifeCycleTest2() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClientBean client = ac.getBean(NetworkClientBean.class);
        ac.close();
    }

    // 3. @PostConstruct, @PreDestroy 사용한 콜백 메서드 호출
    @Test
    public void lifeCycleTest3() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {

        @Bean
        public NetworkClientSpring networkClientSpring() {
            NetworkClientSpring networkClient = new NetworkClientSpring();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }

        @Bean(initMethod = "init", destroyMethod = "close")
        public NetworkClientBean networkClientBean() {
            NetworkClientBean networkClient = new NetworkClientBean();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }

        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}

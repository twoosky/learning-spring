package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SingletonTest {

    /* 결과
    1. init 메서드 한번 호출 -> 인스턴스를 한개만 생성하므로
    2. 스프링 컨테이너 생성 시 빈 등록 -> SingletonBean.init이 find singletonBena1 보다 먼저 출력된다.
    3. 빈 요청을 여러번 해도 같은 인스턴스 빈 반환
     */

    @Test
    @DisplayName("싱글톤 스코프 빈에선 같은 인스턴스의 빈을 공유")
    void singletonBeanTest() {
        // AnnotationConfigApplicationContext 를 통해 수동 빈 등록
        ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);

        System.out.println("find singletonBean1");
        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        System.out.println("find singletonBean2");
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);

        System.out.println("singletonBean1 = " + singletonBean1);
        System.out.println("singletonBean2 = " + singletonBean2);
        Assertions.assertThat(singletonBean1).isSameAs(singletonBean2);
    }

    @Scope("singleton")
    static class SingletonBean {

        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("SingletonBean.destroy");
        }
    }
}

package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class PrototypeTest {

    @Test
    void prototypeBeanFind() {

        /* 결과
        1. 스프링 컨테이너 생성 시점이 아닌, 빈 요청 시점에 프로토타입 빈 생성 -> find prototypeBean1이 PrototypeBean.init 보다 먼저 출력
        2. 빈 요청마다 새로운 인스턴스 빈 생성해 반환
        3. 프로토타입 빈은 스프링 컨테이너가 관리하지 않으므로 @PreDestroy 호출 안됨
         */

        // AnnotationConfigApplicationContext 를 통해 수동 빈 등록
        ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        Assertions.assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

    }

    @Scope("prototype")
    static class PrototypeBean {
        // 인스턴스 생성 후 의존 관계 주입 후 호출되는 메서드
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}

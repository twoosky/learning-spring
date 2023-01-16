package hello.core.scope;

import ch.qos.logback.core.net.server.Client;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("프로토타입 빈과 싱글톤 빈 같이 사용시 문제 - 프로토타입 빈이 재생성되지 않는다.")
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(2);
    }

    @Test
    @DisplayName("문제 해결 1 - 싱글톤 빈이 프로토타입 빈을 사용할 때마다 스프링 컨테이너에 새로 요청해 생성")
    void singletonClientUsePrototype2() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean2.class, PrototypeBean.class);
        ClientBean2 clientBean1 = ac.getBean(ClientBean2.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean2 clientBean2 = ac.getBean(ClientBean2.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(1);
    }

    @Test
    @DisplayName("문제 해결 2 - ObjectProvider 통해 프로토타입 빈을 사용할 때마다 생성")
    void singletonClientUsePrototype3() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean3.class, PrototypeBean.class);
        ClientBean3 clientBean1 = ac.getBean(ClientBean3.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean3 clientBean2 = ac.getBean(ClientBean3.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(1);
    }

    @Test
    @DisplayName("문제 해결 2 - ObjectProvider 통해 프로토타입 빈을 사용할 때마다 생성")
    void singletonClientUsePrototype4() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean4.class, PrototypeBean.class);
        ClientBean4 clientBean1 = ac.getBean(ClientBean4.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean4 clientBean2 = ac.getBean(ClientBean4.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(1);
    }

    // [문제점] 싱글톤 빈과 프로토타입 빈 함께 사용
    // - 프로토타입 빈이 재생성 되지 않는 문제 발생
    @Scope("singleton")  // @Scope의 기본값은 singleton이므로 생략 가능
    static class ClientBean {
        // [comment] 싱글톤 빈 생성 시점에 프로토타입 빈 생성되어 주입
        // - 추후 싱글톤 빈 요청 시 이미 프로토타입 빈이 주입되어 있으므로, 프로토타입 빈이 재생성되지 않는다.
        private final PrototypeBean prototypeBean;

        // [comment] 생성자 의존관계 주입 시 생성자 하나인 경우 @Autowired 생략 가능
        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    // [문제 해결 1]: 애플리케이션 컨텍스트 주입
    // - 애플리케이션 컨텍스트 전체를 주입받고, 프로토타입 빈 사용 시마다 스프링 컨테이너에 빈 생성 요청
    @Scope("singleton")
    static class ClientBean2 {

        @Autowired
        ApplicationContext ac;

        public int logic() {
            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    // [문제 해결 2]: ObjectProvider 사용
    // - ObjectProvider를 사용해 지정한 빈을 컨테이너에서 찾아 사용
    // - 프로토타입 빈은 스프링 컨테이너에서 관리하지 않으므로, .getObject()통해 빈 조회 시마다 프로토타입 빈 생성
    @Scope("singleton")
    static class ClientBean3 {

        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    // [문제 해결 3]: javax.inject.Provider 사용
    @Scope("singleton")
    static class ClientBean4 {

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }

}

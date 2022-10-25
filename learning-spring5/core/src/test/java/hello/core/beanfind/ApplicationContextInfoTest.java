package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {

    // [comment] AnnotationConfigApplicationContext는 ApplicationContext 인터페이스의 구현체 중 하나
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        // [comment] getBeanDefinitionNames(): 스프링에 등록된 모든 빈 이름 조회
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        // [comment] iter + tab 하면 배열을 순회하는 반복문 자동 생성
        for (String beanDefinitionName : beanDefinitionNames) {
            // [comment] 스프링에 등록된 모든 빈 정보를 출력
            // [comment] bean 이름으로 스프링 컨테이너에서 bean 객체 꺼내오기
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name = " + beanDefinitionName + " object = " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            // [comment] ROLE ROLE_APPLICATION: 직접 등록한 빈
            // [comment] ROLE ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }
}

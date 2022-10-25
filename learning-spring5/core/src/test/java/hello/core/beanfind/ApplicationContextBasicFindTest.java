package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    // [comment] 실제 테스트에선 System.out.println() 사용하면 안됨
    // [comment] Assertions을 통해 시스템이 검증하도록 테스트 구현
    @Test
    @DisplayName("애플리케이션 빈 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 빈 조회")
    void findBeanByType() {
        // [comment] 빈 객체 반환 타입으로 빈 조회
        // - 빈 객체 반환 타입: @Bean 어노테이션이 붙은 메소드의 반환 타입
        // - 인터페이스(MemberService.class) 타입으로 조회하면 인터페이스 구현체가 대상이 됨
        MemberService memberService = ac.getBean(MemberService.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 빈 조회")
    void findBeanByName2() {
        // [comment] 빈 객체 반환 타입인 인터페이스가 아닌 구체 타입으로도 빈 조회 가능
        // - 그러나, 구현이 아닌 역할(인터페이스)에 의존해야 하므로 구체 타입으로 빈 조회 하는 것은 좋지 않음!
        // - 반환값이(인스턴스) 다른 구현체가 될 수도 있다. 이때 인터페이스 타입으로 Bean을 조회하면 코드를 변경하지 않아도 되지만,
        //   구체 타입으로 조회하면 코드를 변경해야 함 즉, 유연성이 떨어진다.
        MemberServiceImpl memberService = ac.getBean(MemberServiceImpl.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByNameX() {
        // [comment] 예외를 검증하는 assertThrows는 junit의 Assertions를 import
        // [comment] 예외 발생 NoSuchBeanDefinitionException: No bean named 'xxxxx' available
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("xxxxx", MemberService.class)
        );
    }


}

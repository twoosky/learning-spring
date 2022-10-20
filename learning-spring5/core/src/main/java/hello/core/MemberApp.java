package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    // psvm 치고 엔터치면 public static void main 메소드 자동생성
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        // [comment] ApplicationContext
        // - Spring은 스프링 컨테이너인 ApplicationContext 사용해 Bean 관리
        // - ApplicationContext는 AnnotationConfigApplicationContext 객체의 파라미터로 @Configuration이 붙은 구성 클래스(AppConfig) 주입해 사용
        // - 이를 통해 스프링 컨테이너는 구성 클래스에 @Bean 어노테이션이 붙은 메소드들을 빈으로 등록하고 관리함 (메소드명으로 bean 이름 설정됨)
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        // [comment] 스프링 빈은 applicationContenxt.getBean() 메서드로 찾음, 파라미터는 (빈 이름, 빈 반환 타입)
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + findMember.getName());
    }
}

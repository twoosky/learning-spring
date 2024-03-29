# Spring Framework
[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/dashboard) 정리 문서

## 객체지향 설계와 스프링
**스프링이란?**  
* 필수
  * 스프링 프레임워크
  * 스프링 부트
* 선택
  * 스프링 데이터, 스프링 세션, 스프링 시큐리티, 스프링 배치, 스프링 클라우드 등등
  * 이러한 스프링 관련 프로젝트들은 [spring.io](https://spring.io/projects)에서 확인 가능
* 스프링 프레임워크
  * `핵심 기술`: 스프링 DI 컨테이너, AOP, 이벤트, 기타
  * 웹 기술: 스프링 MVC, 스프링 WebFlux
  * 데이터 접근 기술: 트랜잭션, JDBC, ORM 지원, XML 지원
  * 기술 통합: 캐시, 이메일, 원격접근, 스케줄링
  * 테스트: 스프링 기반 테스트 지원
  * 언어: 코틀린, 그루비
* 스프링 부트
  * 스프링을 편리하게 사용할 수 있도록 지원
  * 단독으로 실행할 수 있는 스프링 애플리케이션을 쉽게 생성
  * Tomcat 같은 웹 서버를 내장해서 별도의 웹 서버를 설치하지 않아도 됨.
  * 손쉬운 빌드 구성을 위한 starter 종속성 제공
  * 스프링과 3rd parth(외부) 라이브러리 자동 구성
  * 간결한 설정
  * 스프링 부트는 스프링 프레임워크과 반드시 함께 사용되어야 한다.
* 스프링 핵심
  * 스프링은 자바 언어 기반의 프레임워크 (자바 특징 - 객체 지향 언어)
  * 스프링은 객체 지향 언어가 가진 강력한 특징을 살려내는 프레임워크
  * 스프링은 좋은 객체 지향 애플리케이션을 개발할 수 있게 도와주는 프레임워크

**좋은 객체 지향 프로그래밍이란?**  
* 객체 지향 특징
  * 추상화
  * 캡슐화
  * 상속
  * `다형성: 객체 지향 핵심 특징`
* 객체 지향 프로그래밍이란
  * 객체 지향 프로그래밍은 컴퓨터 프로그램을 명령어의 목록으로 보는 시각에서 벗어나 여러 개의 독립된 단위, 즉 `객체들의 모임`으로 파악하고자 하는 것이다.
  * 각각의 `객체`는 메시지를 주고받고, 데이터를 처리할 수 있다.
  * 객체 지향 프로그래밍은 프로그램을 `유연`하고 `변경`이 용이하게 만들기 때문에 대규모 소프트웨어 개발에 많이 사용된다.
* 다형성
  * `역할`과 `구현`으로 구분하면 세상이 단순해지고, 유연해지며 변경도 편리해진다.
  * 인터페이스를 구현한(추상메소드를 오버라이딩) 객체를 실행 시점에 유연하게 변경할 수 있다.
    * 실행 시점에 선택된 자식 객체의 오버라이딩 된 메서드를 실행한다.
* 다형성 장점
  * 클라이언트는 대상의 역할(인터페이스)만 알면 된다.
  * 클라이언트는 구현 대상의 내부 구조를 몰라도 된다.
  * 클라이언트는 구현 대상의 내부 구조가 변경되어도 영향을 받지 않는다.
  * 클라이언트는 구현 대상 자체를 변경해도 영향을 받지 않는다.
* 다형성 단점
  * 역할(인터페이스) 자체가 변하면, 클라이언트, 서버 모두에 큰 변경이 발생한다.
    * ex) 역할(자동차 운전)이 비행기 운전으로 바뀌면 다 뜯어 고쳐야됨.
  * 따라서 인터페이스를 안정적으로 잘 설계하는 것이 중요하다.
* 다형성 예시
  * `클라이언트`: 운전자
  * `역할`: 자동차 운전
  * `구현`: 자동차 종류(k3, 아반떼, 테슬라 모델3)
  * 이때 자동차 종류(구현)가 바뀌어도 운전자는 영향을 받지 않는다. 
  * 운전자는 자동차 운전(역할)만 알면 된다.
* 역할과 구현을 분리
  * `역할` = 인터페이스
  * `구현` = 인터페이스를 구현한 클래스, 구현 객체
  * 객체를 설계할 때 `역할`과 `구현`을 명확히 분리
  * 객체 설계시 역할(인터페이스)을 먼저 부여하고, 그 역할을 수행하는 구현 객체 만들기
* 자바 언어의 다형성
  * `인터페이스`를 구현한 객체 * *new AnnotationConfigApplicationContext(AppConfig.class)*인스턴스를 실행 시점에 유연하게 변경할 수 있다.
    * 부모 객체(인터페이스) 참조변수에 자식 객체(인스턴스를 구현한 클래스)의 인스턴스를 대입 가능
  * 다형성의 본질은 *클라이언트를 변경하지 않고, 서버의 구현 기능을 유연하게 변경* 할 수 있는 것이다.
    * 클라이언트는 요청하는 객체, 서버는 응답하는 객체
    * 다형성은 확장 가능한 설계이다. : 인터페이스의 구현체를 무한히 확장 가능
* 스프링과 객체 지향
  * 다형성이 가장 중요하다!
  * 스프링은 다형성을 극대화해서 이용할 수 있게 도와준다.
  * 스프링에서 이야기하는 제어의 역전(IoC), 의존관계 주입(DI)은 다형성을 활용해서 역할과 구현을 편리하게 다룰 수 있도록 지원한다.

**좋은 객체 지향 설계의 5가지 원칙(SOLID)**
* 객체 지향 설계의 5가지 원칙(SOLID)
  * `SRP`: 단일 책임 원칙 (Single responsibility principle)
  * `OCP`: 개방-폐쇄 원칙 (Open/Closed principle)
  * `LSP`: 리스코프 치환 원칙 (Liskov substitution principle)
  * `ISP`: 인터페이스 분리 원칙 (Interface segregation principle)
  * `DIP`: 의존관계 역전 원칙 (Dependency inversion principle)
* SRP 단일 책임 원칙
  * 한 클래스는 하나의 책임만 가져야 한다.
  * 하나의 책임의 중요한 기준은 `변경`이다. 변경이 있을 때 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것이다.
* OCP 개방-폐쇄 원칙
  * 소프트웨어 요소는 `확장에는 열려`있으나, `변경에는 닫혀`있어야 한다.
  * 다형성을 활용해 인터페이스 구현 클래스를 만들어 새로운 기능을 개발할 수 있으나, 구현 객체를 변경하려면 클라이언트 코드를 변경해야 되는 문제점이 있다.
  * 따라서 OCP를 만족하기 위해선 객체를 생성하고, 연관관계를 맺어주는 별도의 조립, 설정자가 필요하다.   
    이를 스프링에서 스프링 컨테이너가 해준다.
* LSP 리스코프 치환 원칙
  * 다형성에서 하위 클래스는 `인터페이스 규약`을 다 지켜야 한다는 것이다.
  * 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다.
* ISP 인터페이스 분리 원칙
  * 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
  * 인터페이스를 분리하면 특정 인터페이스가 변경돼도 다른 인터페이스에 영향을 주지 않는다.
  * 인터페이스가 명확해지고, 대체 가능성이 높아진다.
* DIP 의존관계 역전 원칙
  * `구현 클래스에 의존하지 않고, 인터페이스(역할)에 의존`해야한다는 원칙
  * 추상화에 의존해야하고, 구체화에는 의존하면 안된다.
<br></br>
* 다형성의 문제점
  * 구현 객체를 변경하려면 클라이언트 코드를 변경해야 한다. 
  * OCP, DIP 위반
  > <img src="https://user-images.githubusercontent.com/50009240/187947837-06756ca9-5c3c-44e7-bcfc-844e76f16325.png" width="460" height="250">
  >
  > 위와 같은 구조일 때, MemberService 클라이언트가 클래스를 직접 선택  
  > ```java
  > MemberRepository m = new MemoryMemberRepository(); 
  > ```
  > 구현 클래스를 JdbcMemberRepository()로 변경하고 싶은 경우
  > ```java
  > // MemberRepository m = new MemoryMemberRepository();  // 기존 코드
  > MemberRepository m = new JdbcMemberRepository();  // 변경 코드
  > ```
  > 위와 같이 구현 클래스를 변경하려면 클라이언트 코드를 변경해야 한다. --> OCP 위반  
  > 또한, MemberService는 인터페이스에 의존하지만, 구현 클래스도 동시에 의존한다. --> DIP 위반  
  > 인터페이스인 MemberRepository에만 의존하면 구현체가 없으므로 코드가 안돌아감. 즉, 구현 클래스에도 의존해야하므로 DIP 위반
* 다형성 만으로는 OCP, DIP를 지킬 수 없으므로, 스프링을 활용한다.

**객체지향 설계와 스프링**
* 스프링은 `다형성 + OCP, DIP`를 가능하게 지원해주는 역할이다.
* 스프링은 DI 컨테이너를 통해 의존관계를 연결하고 주입해준다. 이를 활용해 클라이언트 코드의 변경없이 기능을 확장할 수 있다.
* 인터페이스 활용 장점
  * 인터페이스를 활용하면 하부 구현 기술에 대한 선택을 추후 결정할 수 있다. 
  * 예를 들면, 메모리, jdbc, jpa 등 중 어떤 기술을 사용할지에 대한 선택을 미룰 수 있다.
* 인터페이스 활용 단점
  * 이상적으로는 모든 설계에 인터페이스를 부여하는 것이 좋지만, 추상화 비용이 발생한다.
  * 기능을 확장할 가능성이 없다면, 구체 클래스를 직접 사용하고, 향후 꼭 필요할 때 리팩터링해서 인터페이스를 도입하는 것도 방법이다. 

## 스프링 핵심 원리 이해2 - 객체 지향 원리 적용
**관심사의 분리**
* 애플리케이션의 전체 동작 방식을 구성(config)하기 위해, `구현 객체를 생성`하고 `연결`하는 책임을 가지는 별도의 설정 클래스를 만들자.
  * AppConfig 클래스는 애플리케이션의 실제 동작에 필요한 `구현 객체를 생성`한다.
  * AppConfig 클래스는 생성한 객체 인스턴스의 참조(레퍼런스)를 `생성자를 통해 주입(연결)` 해준다.
  * 즉, 객체를 생성하고 연결하는 역할과 실행하는 역할을 명확히 분리할 수 있다.
  * 클라이언트의 입장에서 보면 의존 관계를 외부에서 주입해주는 것 같다고 해서 DI(Dependency Injection), 의존관계 주입이라 한다.
* AppConfig를 통해 `OCP`, `DIP`를 준수할 수 있다.
  * `OCP`: 구현 객체 변경 시 클라이언트의 코드 변경 없이 구성 영역인 AppConfig의 코드만 수정하면 된다.
  * `DIP`: 생성자를 통해 의존성을 주입받으므로 클라이언트는 인터페이스 즉, 추상에만 의존하면 된다.
> 구성 영역은 당연히 변경될 수 있다. 구성 역할을 담당하는 AppConfig를 애플리케이션이라는 공연의 기획자라고 생각하자.   
> 공연 기획자는 공연 참여자인 구현 객체들을 모두 알아야하고, 공연 참여자를 변경할 수도 있다.

**IoC, DI, 그리고 컨테이너**
* 제어의 역전(Inversion of Control)
  * 일반적인 프로그램은 클라이언트 구현 객체가 스스로 필요한 서버 구현 객체를 생성하고, 연결하고, 실행했다.
  * AppConfig가 등장한 이후 구현 객체는 자신의 로직을 실행하는 역할만 담당하고, 프로그램의 제어, 할당, 흐름은 AppConfig(외부 구성 객체)가 담당한다.
  * 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것이 제어의 역전(IoC)이다.
* 프레임워크 vs 라이브러리
  * 프레임워크가 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임워크 (JUnit ..)
  * 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그것은 라이브러리
* 의존관계 주입
  * `의존 관계 주입`은 애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해 클라이언트와 서버의 실제 의존관계가 연결 되는 것
  * 정적인 클래스 의존 관계와, 실행 시점에 결정되는 동적인 객체(인스턴스) 의존 관계 둘을 분리 해서 생각해야 올바른 의존관계 주입이 가능
  * 의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고, 클라이언트가 호출하는 대상의 인스턴스 타입 변경 가능
  * 의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고, 동적인 객체 인스턴스 의존관계를 쉽게 변경 가능 
  * 정적인 클래스 의존관계
    * 클래스 내에서 코드로 보여지는 의존 관계, 애플리케이션을 실행하지 않아도 분석 가능   
    * ex) 상속, 인터페이스 구현, 객체 사용 등
  * 동적인 객체(인스턴스) 의존관계
    * 애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존 관계
* DI 컨테이너(IoC 컨테이너)
  * 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것
  * ApplicationContext를 스프링 컨테이너라고 함
  * 스프링 컨테이너는 `@Configuration` 이 붙은 코드를 설정(구성) 정보로 사용한다. 여기서 @Bean 이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록하고 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라고함
  * 스프링 빈은 `@Bean`이 붙은 메서드의 명을 스프링 빈의 이름으로 사용한다.
  * 스프링 빈은 applicationContext.getBean() 메서드를 사용해 찾을 수 있다.
  * 이전에는 `AppConfig`를 사용해서 필요한 객체를 직접 조회했지만, 스프링 컨테이너를 통해 필요한 스프링 빈(객체)을 찾을 수 있다.

## 스프링 컨테이너와 스프링 빈
**스프링 컨테이너 생성**
* 스프링 컨테이너
  ```java
  // 스프링 컨테이너 생성 방법
  ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
  ```
  * `ApplicationContext`를 스프링 컨테이너라 한다.
  * `ApplicationContext`는 인터페이스이고, `AnnotationConfigApplicationContext`는 구현체 중 하나이다.
  * 스프링 컨테이너는 XML을 기반으로 만들 수 있고, 위와 같이 애노테이션 기반의 자바 설정 클래스로 만들 수 있다
* 스프링 컨테이너의 생성 과정  
  1. 스프링 컨테이너 생성   
     * 스프링 컨테이너를 생성할 때는 구성 정보를 지정해주어야 한다.
     * 구성 정보: @Configuration 어노테이션이 붙은 클래스
     * ex) *new AnnotationConfigApplicationContext(AppConfig.class)*       
  2. 스프링 빈 등록    
     * 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록한다.
     * @Bean이 붙은 메소드를 호출해 *메소드 이름은 빈 이름으로, 메소드 반환값은 빈 객체* 로 스프링 컨테이너에 등록한다.
     * `@Bean(name="~")`을 통해 빈 이름을 직접 부여할 수도 있다.
     * !! *빈 이름은 항상 다른 이름을 부여* 해야 한다.
     * 같은 이름을 부여하면, 다른 빈이 무시되거나, 기존 빈을 덮어버리거나 설정에 따라 오류가 발생한다.\
  3. 스프링 빈 의존관계 설정
     * 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.

**스프링 빈 조회 - 기본**
* 스프링 빈 조회(1개)
  * `ApplicationContext.getBean(빈이름, 타입)`
  * `ApplicationContext.getBean(타입)`
  * 조회 대상 스프링 빈이 없으면 예외 발생
    * NoSuchBeanDefinitionException: No bean named 'xxxxx' available
* 스프링 빈 동일한 타입이 둘 이상 조회
  * 타입으로 조회 시 같은 타입의 스프링 빈이 둘 이상이면 오류가 발생한다. 이때는 빈 이름을 지정하자. 
  * `ApplicationContext.getBeansOfType()`을 사용해 해당 타입의 모든 빈을 조회할 수도 있다.
* 스프링 빈 조회 - 상속관계
  * 부모 타입으로 빈을 조회하면, 자식 타입도 함께 조회된다.
  * 모든 자바 객체의 최고 부모인 `Object` 타입으로 조회하면, 모든 스프링 빈을 조회한다.
    * 자바는 모든 class는 extend Object가 자동으로 들어가 있으므로

**BeanFactory와 ApplicationContext**  
* `BeanFactory` (interface)
  * 스프링 컨테이너의 최상위 인터페이스
  * 스프링 빈을 관리하고 조회하는 역할을 담당한다.
  * *getBean()* 을 제공한다.
* `ApplicationContext` (interface)
  * BeanFactory 기능을 모두 상속받아서 제공한다.
  * BeanFactory뿐만 아니라, 여러 인터페이스도 상속받는다.
    * MessageSource : 국제화 처리를 위한 인터페이스
    * EnvironmentCapable : 환경 변수(로컬, 개발, 운영등을 구분해 처리)
    * ApplicationEventPublisher : 이벤트를 발행하고 구독하는 모델 지원
    * ResourceLoader : 파일,클래스패스,외부 등에서 리소스 편리하게 조회
> BeanFactory를 직접 사용할 일은 거의 없다. 부가기능이 포함된 ApplicationContext를 사용한다.  
> BeanFactory나 ApplicationContext를 스프링 컨테이너라 한다. 

**다양한 설정 형식 지원 - 자바 코드, XML**
* 애노테이션 기반 자바 코드 설정 사용
  * `AnnotationConfigApplicationContext` 클래스를 사용하면서 자바 코드로된 설정 정보를 넘긴다.
  * ex) new AnnotationConfigApplicationContext(AppConfig.class)
* XML 설정
  * `GenericXmlApplicationContext` 클래스를 사용하면서 xml 설정 파일을 넘김 

**BeanDefinition**  
* BeanDefinition: 빈 설정 메타정보이다. `@Bean`, `<bean>` 당 각각 하나씩 메타 정보가 생성된다.
* 스프링 컨테이너는 이 메타정보를 기반으로 스프링 빈을 생성한다.
* 스프링은 다양한 형태의 설정 정보를 BeanDefinition으로 추상화해서 사용한다.

## 싱글톤 컨테이너
**싱글톤 패턴**  
* 웹 애플리케이션과 싱글톤
  * 웹 애플리케이션은 보통 동시에 여러 요청이 들어온다.
  * 요청을 할 때마다 객체가 새로 생성되면 메모리 낭비가 심하므로, 스프링에서는 해당 객체가 딱 1개만 생성되고, 공유하도록 설계하는 `싱글톤` 방식을 이용한다.
* 싱글톤 패턴
  * 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴이다.
  * private 생성자를 사용해서 외부에서 임의로 new 키워드를 사용하지 못하도록 막아야 한다.
  ```java
  public class SingletonService {

      // 1. static 영역에 객체를 딱 1개만 생성해둔다.
      private static final SingletonService instance = new SingletonService();

      // 2. 객체 인스터스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용한다.
      public static SingletonService getInstance() {
          return instance;
      }

      // 3. 생성자를 private으로 막아 외부에서 new 키워드로 객체 인스턴스가 생성되는 것을 막는다.
      private SingletonService() {
      }
  }
  ```
* 싱글톤 패턴의 문제점
  * 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
  * 의존관계상 클라이언트가 구체 클래스에 의존한다 (DIP/OCP 위반)
  * private 생성자로 자식 클래스를 만들기 어렵다.

**싱글톤 컨테이너**  
* 스프링 컨테이너
  * 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤(1개만 생성)으로 관리한다.
  * 스프링 컨테이너는 `싱글톤 컨테이너` 역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라 한다.
  * 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 된다.
  * DIP, OCP, 테스트, private 생성자로붜 자유롭게 싱글톤을 사용할 수 있다.
  * 스프링의 기본 빈 등록 방식은 싱글톤이지만, 싱글톤 방식만 지원하는 것은 아니다. 요청할 때마다 새로운 객체를 생성해서 반환하는 기능도 제공
* `싱글톤 방식의 주의점`
  * 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 *상태를 유지(stateful)하게 설계하면 안된다.*
* `무상태(stateless)`로 설계하기 위한 방법
  * 특정 클라이언트에 의존적인 필드가 있으면 안된다.
  * 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
  * 가급적 읽기만 가능해야 한다.
  * 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, TreadLocal등을 사용해야 한다.

**@Configuration과 싱글톤**  
* 스프링 컨테이너는 싱글톤 레지스트리이므로 스프링 빈의 싱글톤을 보장한다.
* 스프링은 `@Configuration`을 붙이면 `CGLIB`라는 바이트코드 조작 라이브러리를 사용해서 실제 클래스를 상속받은 임의의 다른 클래스를 만든다. 이를 다른 클래스를 스프링 빈으로 등록해 싱글톤이 되도록 보장해준다.
* `CGLIB` 라이브러리를 사용해 `@Bean`이 붙은 메서드마다 이미 스프링 빈이 존재하면, 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환함으로써 싱글톤을 보장한다.
* @Configuration을 적용하지 않고, @Bean만 적용하는 경우 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.

## 컴포넌트 스캔
**컴포넌트 스캔과 의존관계 자동 주입**  
* 컴포넌트 스캔: 설정 정보 없이 자동으로 스프링 빈을 등록하는 기능
* 의존관계도 자동으로 주입하는 `@Autowired` 기능도 제공한다.
* 컴포넌트 스캔
  * 컴포넌트 스캔은 `@Component` 어노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다.
  * @Configuration도 컴포넌트 스캔 대상이다. 내부적으로 @Component 어노테이션이 붙어있기 때문
  * @ComponentScan은 스프링 빈을 등록할 때 기본 이름은 클래스명을 사용하되, 맨 앞글자만 소문자를 사용한다.
* 의존관계 자동 주입
  * 컴포넌트 스캔을 사용하면 스프링 빈이 자동으로 등록되는데, 의존 관계를 수동으로 설정할 수 없어 의존관계 자동 주입 @Autowired 를 사용한다.
  * 생성자에 `@Autowired`를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다.
  * 이때, 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다. `getBean(타입)`과 동일

**탐색 위치와 기본 스캔 대상**
* 탐색 위치
  * `basePackages`: 탐색할 패키지의 시작 위치를 지정 가능. 이 패키지를 포함해서 하위 패키지를 모두 탐색
    * basePackages = {"hello.core", "hello.service"} 와 같이 여러 시작 위치를 지정할 수 도 있다.
  * `basePackClasses`: 지정한 클래스의 패키지를 탐색 시작 위치로 지정
  * 위 설정들을 지정하지 않으면(default), @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.
    * 설정 정보 클래스: AutoAppConfig.class ..
  ```java
  @ComponentScan(
        basePackages = "hello.core",
        basePackageClasses = AutoAppConfig.class
  )
  ```
  * `권장`: 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두자.  
    * 참고로, 스프링 부트의 대표 시작 정보인 @SpringBootApplication를 프로젝트 시작 루트 위치에 두는 것이 관례이다. 
    * 시작 설정 내부에도 @ComponentScan이 들어있다!
* 컴포넌트 스캔 기본 스캔 대상
  * `@Component`: 컴포넌트 스캔에서 사용하는 기본 설정
  * `@Controlller`: 스프링 MVC 컨트롤러에서 사용, 스프링 MVC 컨트롤러로 인식
  * `@Service`: 스프링 비즈니스 로직에서 사용
  * `@Repository`: 스프링 데이터 접근 계층에서 사용, 스프링 데이터 접근 계층으로 인식하고 데이터 계층의 예외를 스프링 예외로 변환
  * `@Configuration`: 스프링 설정 정보에서 사용, 스프링 설정 정보로 인식하고 스프링 빈이 싱글톤을 유지하도록 추가 처리
  
**필터**
* `incloudefilters`: 컴포넌트 스캔 대상을 추가로 지정
* `excloudeFilters`: 컴포넌트 스캔에서 제외할 대상 지정
```java
@ComponentScan(
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
```
* 필터 옵션
  * ANNOTATION: 기본값, 애노테이션을 인식해서 동작
  * ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작
  * ASPECTJ: AspectJ 패턴 사용
  * REGEX: 정규 표현식
  * CUSTOM: TypeFilter 라는 인터페이스를 구현해서 처리

**중복 등록과 충돌**  
* 컴포넌트 스캔에서 같은 빈 이름을 등록하면 어떻게 될까?
* 자동 빈 등록 vs 자동 빈 등록
  * 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 그 이름이 같은 경우 *ConflictingBeanDefinitionException* 발생
* 수동 빈 등록 vs 자동 빈 등록
  * 스프링에서는 수등 빈 등록이 우선권을 갖으나, 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생한다. (우선권이 주어질 시 어려운 버그가 발생할 수 있으므로)
  * 스프링 부트에서 프로퍼티에 spring.main.allow-bean-definition-overriding=true를 통해 수동 빈이 자동 빈을 오버라이딩하게도 설정 가능 

## 의존관계 자동 주입
**의존관계 주입 방법**
1. 생성자 주입
    * 생성자를 통해 의존 관계를 주입 받는 방법
    * 컴포넌트가 스프링 빈에 등록될 때 생성자가 호출되는데, 그때 스프링 컨테이너에서 필요한 의존관계들을 주입
    * `불변, 필수` 의존관게에서 사용!
    * 생성자가 단 1개라면 스프링 빈 등록 시 @Autowired 생략 가능!
    ```java
    @Component
    public class OrderServiceImpl implements OrderService {
        private final MemberRepository memberRepository;
        private final DiscountPolicy discountPolicy;
        
        @Autowired
        public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
    }
    ```
2. 수정자 주입
    * setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법
    * `선택, 변경` 가능성이 있는 의존관계에서 사용
    ```java
    @Component
    public class OrderServiceImpl implements OrderService {

        private MemberRepository memberRepository;
        private DiscountPolicy discountPolicy;

        @Autowired
        public void setMemberRepository(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }

        @Autowired
        public void setDiscountPolicy(DiscountPolicy discountPolicy) {
            this.discountPolicy = discountPolicy;
        }
    }
    ```
    * 자바빈 프로퍼티 규약: 필드의 값을 직접 변경하지 않고, setXxx, getXxx 라는 메서드를 통해서 값을 읽거나 수정하는 규칙
    * 수정자 주입은 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.

3. 필드 주입
    * 필드에 의존 관계를 주입하는 방법
    * 코드가 간결하지만, 외부에서 직접 의존관계 주입을 할 수 없어 테스트 하기 힘들다는 치명적 단점이 있다.
    * DI 프레임워크가 없으면 아무것도 할 수 없는 코드가 된다.
    * 애플리케이션의 실제 코드와 관계 없는 테스트 코드에서만 사용하자!
    ```java
    @Autowired private MemberRepository memberRepository;
    @Autowired private DiscountPolicy discountPolicy;
    ```
4. 일반 메서드 주입
    * 일반 메서드를 통해 의존 관계를 주입받는 방법
    * 일반적으로 잘 사용하지 않는다.

> 의존관계 주입 주의 사항
> * `의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작` 한다.  
> * 스프링 빈이 아닌 클래스에서 @Autowired 를 적용한 경우 아무 기능을 하지 않는다.

**옵션 처리**  
* 주입할 스프링 빈이 없어도 동작해야 하는 경우 사용
* `@Autowired`만 사용하는 경우 required 옵션의 기본값이 true로 되어 있어 자동 주입 대상이 없으면 오류가 발생한다. 이를 방지하기 위해 옵션 처리
* 옵션 처리 방법
  * @Autowired(required = false): 자동 주입할 대상이 없으면 메서드 자체가 호출이 안된다.
  * org.springframework.lang.@Nullable: 자동 주입할 대상이 없으면 null이 입력된다.
  * Optional<>: 자동 주입할 대상이 없으면 Optional.empty가 입력된다.
* 옵션 처리 예시
```java
static class TestBean {
   @Autowired(required = false)
   public void setBean1(Member noBean1) {
       System.out.println("noBean1 = " + noBean1);
   }

   @Autowired
   public void setBean2(@Nullable Member noBean2) {
       System.out.println("noBean2 = " + noBean2);
   }

   @Autowired
   public void setBean3(Optional<Member> noBean3) {
       System.out.println("noBean3 = " + noBean3);
   }
}
```
```
// output
noBean2 = null
noBean3 = Optional.empty
```
  * Member는 스프링 빈이 아니므로, 의존 관계를 주입할 수 없다.
  * 따라서 require = false 속성을 사용한 setBean1 메서드는 메서드 호출 자체가 되지 않았다.
  
**생성자 주입을 선택해야 하는 이유**  
1. 불변
    * 대부분의 의존관계는 한번 주입되고 나서 애플리케이션 종료시점까지 변경할 일이 없다.
    * 수정자 주입을 사용하면, setXxx 메서드를 public으로 열어두어야 한다. 
    * 누군가 실수로 변경할 수도 있고, 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아니다.
    * `생성자 주입`은 객체를 생성할 때 딱 1번 호출되므로, `불변`하게 설계할 수 있다.
2. 누락
    * 생성자 주입을 사용하면 주입 데이터를 누락했을 때 컴파일 오류가 발생한다.
    * 즉, IDE에서 바로 어떤 값을 필수로 주입해야 하는지 알 수 있다.
3. final
    * 생성자 주입을 사용하면 필드에 fianl 키워드를 사용할 수 있다.
    * 따라서 `생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.`
    * 생성자 주입 외 주입 방식은 모두 생성자 이후에 호출되므로, 필드에 final 키워드를 사용할 수 없다.
> TIP
> * 컴파일 오류가 세상에서 가장 빠르고, 좋은 오류다!  
> * 항상 생성자 주입을 선택해라! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해라. 필드 주입은 사용하지 않는게 좋다.

**롬복과 생성자 의존관계 주입**
* 롬복 라이브러리가 제공하는 `@RequiredArgsConstructor` 기능을 사용하면 final이 붙은 필드를 모아 컴파일 시점에 생성자를 자동으로 만들어준다.
* 이를 통해 의존관계 주입을 편리하게 할 수 있다.

**조회 빈이 2개 이상인 경우 문제점** 
* 기본적으로 bean은 타입으로 조회한다. 이때, 조회할 빈의 하위 타입 빈이 여러 개인 경우 NoUniqueBeanDefinitionException 오류가 발생한다.
* 예시)
  * DiscountPolicy의 하위 타입인 FixDiscountPolicy, RateDiscountPolicy 둘 다 스프링 빈으로 선언해보자.
  ```java
  @Component
  public class FixDiscountPolicy implements DiscountPolicy { }
  ```
  ```java
  @Component
  public class RateDiscountPolicy implements DiscountPolicy { }
  ```
  * 그리고 이렇게 의존관계 자동 주입을 실행하면, NoUniqueBeanDefinitionException 오류가 발생한다.
  ```java
  @Autowired
  private DiscountPolicy discountPolicy
  ```
* 이때 하위 타입으로 의존관계 주입을 할 수도 있지만, 하위 타입으로 지정하는 것은 DIP를 위배하고, 유연성이 떨어진다.

**조회 빈이 2개 이상인 경우 해결방법**
1. @Autowired 필드 명 매칭
    * @Autowired 는 타입 매칭을 시도하고, 이때 여러 빈이 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가 매칭한다.
    ```java
    // 생성자 주입 시 파라미터 이름으로 매칭
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy rateDiscountPolicy)     {
        this.discountPolicy = rateDiscountPolicy;
        this.memberRepository = memberRepository;
    }
    ```
    ```java
    // 필드 주입 시 필드 이름으로 매칭
    @Autowired
    private DiscountPolicy rateDiscountPolicy
    ```
2. @Qualifier
    * 추가 구분자를 붙여주는 방법이다. 주입 시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다.
    * @Qualifier로 의존관계 주입 시 지정한 빈을 찾지 못한다면 해당 이름의 스프링 빈을 추가로 찾는다. 
    * 어떠한 스프링 빈도 찾지 못하는 경우 NoSuchBeanDefinitionException 예외 발생
    * @Qualifier는 @Qualifier를 찾는 용도로만 사용하는게 명확하고 좋다.
    ```java
    @Component
    @Qualifier("mainDiscountPolicy")
    public class RateDiscountPolicy implements DiscountPolicy{
        ...
    }
    ```
    ```java
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }
    ```
    * @Qualifier의 단점은 위와 같이 모든 코드에 @Qualifier를 붙여줘야 한다.
3. @Primary
    * 의존관계 주입 시 같은 타입의 빈이 여러 개인 경우 우선순위를 지정하는 방법
    ```java
    @Component
    @Primary
    public class RateDiscountPolicy implements DiscountPolicy{
        ...
    }
    ```
    * @Primary보다 @Qualifier의 우선순위가 더 높다.

**@Qualifier 애노테이션 직접 만들기**
* `@Qualifier("mainDiscountPolicy")` 이렇게 직접 문자열을 적으면 컴파일 시 타입 체크가 안된다.
* 다음과 같이 애노테이션을 만들어 문제를 해결할 수 있다.
```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
```
```java
@Component
@MainDiscountPolicy  // @Qualifier("mainDiscountPolicy") 대신 커스텀 어노테이션 사용
public class RateDiscountPolicy implements DiscountPolicy{
```
```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
    this.discountPolicy = discountPolicy;
    this.memberRepository = memberRepository;
}
```
**자동, 수동 빈 등록의 올바른 실무 운영 기준**  
* 편리한 자동 빈 등록 기능을 기본으로 사용하자
  * 스프링 빈을 하나 등록할 때 `@Component`만 넣어주면 끝나느 일을 `@Configuration` 설정 정보에 가서 `@Bean`을 적고, 객체를 생성하고, 주입할 대상을 일일이 적어주는 과정은 상당히 번거롭다.
  * 결정적으로 자동 빈 등록을 사용해도 OCP, DIP를 지킬 수 있기 때문에 자동 빈 등록을 사용하자
* 수동 빈 등록은 언제 사용하면 좋을까
  * 기술 지원 빈: 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나, 공통 로그 처리 처럼 업무 로직 지원을 위한 하부 기술이나 공통 기술이다.
  * 애플리케이션에 광범위하게 영향을 미치는 기술 지원 객체는 수동 빈으로 등록해서 설정 정보에 명확하게 들어내는 것이 유지보수하기 좋다.
  * 비즈니스 로직 중에서 다형성을 적극 활용하는 경우 설정 정보(@Configuration)만 보고 한 눈에 파악하기 위한 수동 빈 등록을 고민해볼 수 있다.
  * 위 경우 수동 빈 등록 또는 자동 빈 등록 객체들을 특정 패키지에 같이 묶어두는게 좋다!

## 빈 생명주기 콜백
**빈 생명주기**  
* 빈 생명 주기: 스프링에서 객체의 초기화 작업과 종료 작업을 처리할 때 사용하는 라이프 사이클
* 스프링 빈 라이프사이클
  * `객체 생성 -> 의존관계 주입`
  * 스프링 빈은 객체를 생성하고, 의존관계 주입이 다 끝난 다음에야 필요한 데이터를 사용할 수 있는 준비가 완료된다.
  * 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음 호출해야 한다.
  * 스프링은 의존관계 주입이 완료되면 스프링 빈에게 `콜백 메서드`를 통해 초기화 시점을 알려주는 기능이 있다.
  * 스프링은 스프링 컨테이너가 종료되기 직전에 `소멸 콜백`을 준다. 따라서 안전하게 종료 가능
* 스프링 빈의 이벤트 라이프사이클
  * `스프링 컨테이너 생성` -> `스프링 빈 생성` -> `의존관계 주입` -> `초기화 콜백` -> `사용` -> `소멸전 콜백` -> `스프링 종료`
> `TIP` 객체의 생성과 초기화를 분리하자!
> * 생성자는 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가진다.
> * 초기화는 이렇게 생성된 값을 활용해서 외부 커넥션을 연결하는 등 무거운 동작을 수행한다.
> * 따라서 객체 생성과 초기화를 분리하는 것이 유지보수 관점에서 좋다.

**빈 생명주기 콜백 지원 방식**  
1. 인터페이스(InitializingBean, DisposableBean)
    * 외부 라이브러리에 적용할 수 없어 거의 사용하지 않는 방법이다.
    * InitializingBean은 afterPropertiesSet() 메서드로 초기화 콜백 지원
    * DisposableBean은 destroy() 메서드로 소멸 콜백 지원
2. 설정 정보에 초기화 메서드, 종료 메서드 지정
    * 설정 정보에 `@Bean(initMethod = "함수 이름", destroyMethod = "함수 이름")` 를 사용해 초기화, 소멸 메서드를 지정할 수 있다.
    * 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.
    * @Bean의 destroyMethod 속성은 기본값이 `(inferred)` (추론)으로 등록되어 있다.
    * 이 추론 기능은 close, shutdown 라는 이름의 메서드를 자동으로 호출해준다.
    * 따라서 직접 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 잘 동작한다.
3. 어노테이션 @PostConstruct, @PreDestory
    * 스프링에서 권장하는 방법이다.
    * 초기화 메서드에 @POstConstruct, 소멸 메서드에 @PreDestory 어노테이션을 붙이면 해당 시점에 자동 호출된다.
    * javax.annotation에 존재하는 자바 표준으로 스프링에 의존적이지 않다.
    * 외부 라이브러리를 초기화할 수 없다는 단점이 있다. 외부 라이브러리 초기화, 종료 시에는 @Bean의 기능을 사용하자
    ```java
    public class NetworkClient {
        private String url;

        public NetworkClient() {
            System.out.println("생성자 호출, url = " + url);
        }
    
        public void setUrl(String url) {
            this.url = url;
        }

        public void connect() {
            System.out.println("connect: " + url);
        }

        public void call(String message) {
            System.out.println("Call " + url + " message = " + message);
        }

        public void disconnect() {
            System.out.println("close " + url);
        }

        @PostConstruct
        public void init() {
            System.out.println("NetworkClient.afterPropertiesSet");
            connect();
            call("초기화 연결 메시지");
        }

        @PreDestroy
        public void close() {
            System.out.println("NetworkClient.destroy");
            disconnect();
        }
    }
    ```
    ```java
    @Configuration
    static class LifeCycleConfig {
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient(); 
            networkClient.setUrl("http://hello-spring.dev"); 
            return networkClient;
        }
    }
    ```
> `TIP`
> * @PostConstruc, @PreDestory 애노테이션을 사용하자
> * 코드를 고칠 수 없는 외부라이브러리를 초기화, 종료해야되는 경우 @Bean의 initMethod, destroyMethod 속성을 사용하자.

## 빈 스코프
**빈 스코프**  
* 빈 스코프는 `빈이 존재할 수 있는 범위`를 의미한다.
* 스프링 빈은 기본적으로 싱글톤 스코프로 생성되어 스프링 컨테이너의 시작과 함께 빈이 생성되고, 스프링 컨테이너가 종료될 때까지 유지한다.
* 스코프 종류
  * `싱글톤 스코프`: 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프
  * `프로토타입 스코프`: 스프링 컨테이너는 *프로토타입 빈의 생성과 의존관계 주입까지만 관여 하고, 더는 관리하지 않는* 매우 짧은 범위의 스코프이다.
  * `웹 관련 스코프
    * request: 웹 요청이 들어오고 나갈때까지 유지되는 스코프
    * session: 웹 세션이 생성되고 종료될 때까지 유지되는 스코프
    * application: 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프
* 빈 스코프 지정 방법
```java
// 1. 컴포넌트 스캔 자동 등록
```java
@Scope("prototype")
@Component
public class HelloBean {}
```
```java
// 2. 수동 등록
@Scope("prototype")
@Bean
PrototypeBean HelloBean() {
    return new HelloBean();
}
```

**프로토 타입 스코프**
* 프로토 타입 빈 특징
  * 스프링 컨테이너에 요청할 때마다 빈 새로 생성
  * 스프링 컨테이너는 프로토타입의 빈 생성과 의존관계 주입, 초기화까지 관여
  * 종료 메서드가 호출되지 않는다.
  * 그래서 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 관리해야 한다. 종료 메서드에 대한 호출도 클라이언트가 직접 해야 한다.
* 싱글톤 스코프의 빈과 프로토타입 스코프의 빈 차이
  * 싱글톤 빈은 스프링 컨테이너 생성 시점에 빈 생성 및 초기화 메서드 실행
  * 프로토타입 빈은 스프링 컨테이너에서 빈을 조회할 때 생성 및 초기화 메서드 실행
  * 싱글톤 빈은 빈을 조회하면 스프링 컨테이너에서 항상 같은 인스턴스의 스프링 빈을 반환
  * 프로토타입 스코프의 빈을 요청하면 스프링 컨테이너에서 항상 새로운 인스턴스를 생성해서 반환
  * 싱글톤 빈은 종료 메서드를 실행하지만, 프로토타입 빈에서는 종료메서드(@PreDestroy)가 실행되지 않는다. 수동으로 호출해야 함
* 프로토 타입 빈 요청 과정
  * 1. 프로토타입 스코프의 빈을 스프링 컨테이너에 요청한다.
  * 2. 스프링 컨테이너는 이 시점에 프로토타입 빈을 생성하고, 필요한 의존관계를 주입한다.
  * 3. 스프링 컨테이너는 생성한 프로토타입 빈을 클라이언트에 반환한다.
  * 4. 프로토타입 빈은 스프링 컨테이너가 관리하지 않으므로, 이후에 스프링 컨테이너에 같은 요청이 오면 항상 새로운 프로토타입 빈을 생성해서 반환한다.

**프로토타입 스코프 빈을 싱글톤 빈과 함께 사용시 문제점**
* 싱글톤 빈은 스프링 컨테이너 생성 시점에 함께 생성되고, 의존관게 주입도 발생한다.
* 의존관계 자동 주입 시점에 스프링 컨테이너에 프로토타입 빈을 요청한다.
* 스프링 컨테이너는 프로토타입 빈을 생성해서 싱글톤 빈에 반환하고, 싱글톤 빈은 이를 내부 필드에 보관한다. (참조값 보관)
* 싱글톤 빈의 내부에 가지고 있는 프로토타입 빈은 이미 과거에 주입이 끝난 빈이기 때문에, `싱글톤 빈을 요청해도 프로토타입 빈은 새로 생성되지 않는다.`
* 즉, 프로토타입 빈은 싱글톤 빈과 함께 계속 유지된다.

**문제 해결 방법**
1. 싱글톤 빈이 프로토타입을 사용할 때마다 스프링 컨테이너에 새로 요청하게 변경
    ```java
    @Autowired
    ApplicationContext ac;

    public int logic() {
        PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }
    ```
    * 스프링 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워진다.
2. `ObjectProvider`
    * 지정한 빈을 컨테이너에서 대신 찾아주는 DL(Dependency Lookup) 서비스를 제공하는 객체이다.
    * 프로토타입 빈은 스프링 컨테이너가 관리하지 않으므로, prototypeBeanProvider.getObject()를 통해 항상 새로운 프로토타입 빈이 생성된다.
    * ObjectFactory의 확장, 스프링에 의존적
    * 권장되는 방법
    ```java
    @Autowired
    private ObjectProvider<PrototypeBean> prototypeBeanProvider;

    public int logic() {
        PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
        prototypeBean.addCount();
        int count = prototypeBean.getCount();
        return count;
    }
    ```
3. JSR-330 Provider
    * `javax.inject.Provider` 라는 JSR-330 자바 표준을 사용하는 방법
    * 스프링에 의존적이지 않음.
    * 별도의 라이브러리가 필요하다는 단점이 있다.
    
**웹 스코프**  
* `웹 스코프`: 웹 환경에서만 동작하는 스코프
* 웹 스코프는 스프링이 해당 스코프의 종료시점까지 관리한다. 따라서 종료 메서드가 호출된다.
* 웹 스코프 종류
  * `request`: HTTP 요청 하나가 들어오고 나갈 때까지 유지되는 스코프, 각각의 *HTTP 요청마다 별도의 빈 인스턴스가 생성되고, 관리된다.*
  * `session`: HTTP Session과 동일한 생명주기를 갖는 스코프
  * `application`: 서블릿 컨텍스트(`ServletContext`)와 동일한 생명주기를 갖는 스코프
  * `websocket`: 웹 소켓과 동일한 생명주기를 갖는 스코프
* 웹 환경 추가
  ```groovy
  // web 라이브러리 추가
  implementation 'org.springframwork.boot:spring-boot-starter-web'
  ```
  * 위 라이브러리를 추가하면 스프링 부트는 `내장 톰켓 서버`를 활용해 웹 서버와 스프링을 함께 실행시킨다.
  * 스프링 부트는 웹 라이브러리가 없으면 `AnnotationConfigApplicationContext`을 기반으로 애플리케이션을 구동한다.
  * 웹 라이브러리가 추가되면 `AnnotationConfigServletWebServerApplicationContext`를 기반으로 애플리케이션을 구동한다.

**request 스코프 빈을 싱글톤 빈과 함께 사용시 문제점**
  * 스프링 애플리케이션을 실행하는 시점에 싱글톤 빈은 생성되어 의존관계 주입을 해야한다.
  * 하지만, request 스코프 빈은 실제 HTTP 요청이 와야 빈이 생성되므로 싱글톤 빈의 의존관계로 주입이 될 수 없다.
  * 따라서 HTTP 요청이 올때 까지 request 스코 빈의 조회 지연 처리가 필요하다.

**문제 해결 방법**
1. `ObjectProvider`를 통해 HTTP 요청 들어오는 시점까지 빈 조회 지연
2. 프록시 사용
    * `proxyMode`를 통해 가짜 `프록시 클래스`를 만들어두고, HTTP request와 상관없이 가짜 프록시 클래스를 다른 빈에 미리 의존관계 주입해둘 수 있다.
    * 즉, CGLIB라는 라이브러리로 내 클래스를 상속받은 가짜 프록시 객체를 만들어 주입
    * 그 후, 가짜 프록시 객체에 실제 요청이 오면 내부에 실제 빈 요청을 위임한다.
    ```java
    @Component
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public class MyLogger {
    }
    ```



























> intellij 단축키
> * `command+shift+enter`: 구문 자동완성 후 다음 줄로 넘어감
> * `ctrl+alt+v`: 변수 추출하기
> * `psvm`: public static void main(String[] args) 자동 생성
> * `soutv`: 변수에 대한 System.out.println() 자동 생성
> * `soutm`: 메소드에 대한 System.out.println() 자동 생성






































  

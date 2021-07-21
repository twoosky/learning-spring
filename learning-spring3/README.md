Spring Boot
===
[Spring Boot를 이용한 RESTful Web Services 개발](https://www.inflearn.com/course/spring-boot-restful-web-services/dashboard) 정리 문서

Spring Boot RESTful
===
* Spring Boot
  * 스프링 기반의 단독 실행 가능한 어플리케이션을 최소한의 설정으로 개발하기 위한 플랫폼  
  * `@SpringBootApplication` 어노테이션을 갖고 있는 main 클래스를 통해 실행
  * `Auto Configuration` 기능을 통해 스프링 어플리케이션에 필요한 설정 작업을 자동화 할 수 있음.
  * `Component Scan` 특정 클래스의 instance를 spring container의 메모리로 읽어와 app에서 사용가능한 bean형태로 등록
  * <https://start.spring.io/> 에서 spring boot 프로젝트 생성
  * IoC:  프로그래밍에 의해 클래스의 instance를 생성하는 것이 아니라, spring container에 의해 instance가 생성되고 관리되는 것
* Spring Boot Project
  * Maven을 이용한 Spring Boot 프로젝트 구조
    * src/main/java: java 클래스 파일
    * src/main/resources: static, html 파일, 환경설정 파일
    * srt/test: 단위테스트에 필요한 테스트 코드를 담은 파일
    * pom.xml: 프로젝트에 필요한 maven 설정 파일
  * Maven 명령어
    * Lifecycle/clean: 지금까지 build, packaging 시켰던 파일의 내용을 모두 지움.
    * Lifecycle/compile: 작성한 프로젝트를 컴파일, ByteCode(class file)이 생성됨.  
    * Lifecycle/package: 컴파일한 내용을 jar/war 파일로 패키징 함.
    * Lifecycle/install: packaging시킨 내용을 로컬 서버에 배포
  * pom.xml 구조
    * 스프링 부트에 대한 정보
    * 스프링 부트 프로젝트 라이브러리 항목
  * Spring Boot Project 실행
    * 기본적으로 embedded tomcat이 내장되어 있음.
    * spring boot에 web dependency를 포함시킨 상태에서 app을 실행하면 void main이 실행됨. 이때 main 클래스 내부에서 app실행에 필요한 embedded tomcat을 같이 구동
    * web server를 구동하지 않은 상태에서 단순히 java 프로그램 하나만 실행시켜 같이 서비스를 구동할 수 있음.
  * application.properties
    * src/main/resources/application.properties
    * Spring Boot Project 실행에 필요한 환경 설정을 지정하는 파일 
* lombok
  * `@Data`: setter, getter, toString, equals 등의 메소드 자동 생성
  * `@AllArgsConstructor`: 클래스의 멤버변수의 값을 setting해주는 생성자 자동 생성
  * `@NoArgsConstructor`: 매개변수 없는 default 생성자 자동 생성
* DispatcherServlet
  * 클라이언트의 모든 요청을 한곳으로 받아서 처리
  * servlet container에서 http 프로토콜을 통해 들어오는 모든 요청값을 처리하기 위해서 프레젠테이션 계층의 제일 앞에 놓여지며 중앙 집중식의 요청 처리를 하기 위한 front controller로서 사용됨. 일종의 GateWay 역할
  
  <img src="https://media.vlpt.us/images/ehdrms2034/post/e602b868-b975-4d1b-97ed-a0d3e2d9f4c4/image.png" width="50%" height="40%" title="px(픽셀) 크기 설정" alt="DispatcherServlet"></img>
  
  1. 모든 사용자의 Request가 Dispatcher Servlet에 전달
  2. Dispatcher Servlet가 사용자 요청을 HandlerMapping에 전달 
  3. Controller에 처리 요청 
  4. 처리된 결과값을 Spring MVC에서는 Model형태로 반환해줌. 
  5. ViewResolver가 page 형식에 따라 page 생성 
  6. page값에 Model을 포함시켜 전달
* RestController
  * `@RestController`: `@Controller`와 `@ResponseBody`가 결합된 어노테이션
  * View를 갖지 않는 REST Data(JSON/XML)를 반환하는 controller
  * `@RestController` 어노테이션을 클래스에 등록하면 모든 핸들러 메소드에서 `@ResponseBody`를 사용할 필요 없음.
  * Client -> http Request -> DispathcerServlet ->HandlerMapping -> REST Controller -> http Response
* Path Variable
  * Mapping 어노테이션의 URI인자에 {변수명}을 설정해 URI에 가변데이터를 사용하는 것
  * `@PathVariable`: 핸들러 메소드의 파라미터를 통해 가변 데이터 받음.
  * 가변 데이터는 클라이언트에 의해 설정됨.

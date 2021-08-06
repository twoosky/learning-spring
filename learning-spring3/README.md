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
  * 클라이언트의 모든 요청을 한곳으로 받아서 처리하는 GateWay 역할
  * servlet container에서 http 프로토콜을 통해 들어오는 모든 요청값을 처리하기 위해서 프레젠테이션 계층의 제일 앞에 놓여지며 중앙 집중식의 요청 처리를 하기 위한 front controller로서 사용됨. 
  * 동작 과정
    * 모든 사용자의 Request가 Dispatcher Servlet에 전달
    * Dispatcher Servlet가 사용자 요청을 HandlerMapping에 전달 
    * Controller에 처리 요청 
    * 처리된 결과값을 Spring MVC에서는 Model형태로 반환해줌. 
    * ViewResolver가 page 형식에 따라 page 생성 
    * page값에 Model을 포함시켜 전달  
  <img src="https://media.vlpt.us/images/ehdrms2034/post/e602b868-b975-4d1b-97ed-a0d3e2d9f4c4/image.png" width="45%" height="35%" title="px(픽셀) 크기 설정" alt="DispatcherServlet"></img>  
* RestController
  * `@RestController`: `@Controller`와 `@ResponseBody`가 결합된 어노테이션
  * View를 갖지 않는 REST Data(JSON/XML)를 반환하는 controller
  * `@RestController` 어노테이션을 클래스에 등록하면 모든 핸들러 메소드에서 `@ResponseBody`를 사용할 필요 없음.
  * Client -> http Request -> DispathcerServlet ->HandlerMapping -> REST Controller -> http Response
* Path Variable
  * Mapping 어노테이션의 URI인자에 {변수명}을 설정해 URI에 가변데이터를 사용하는 것
  * `@PathVariable`: 핸들러 메소드의 파라미터를 통해 가변 데이터 받음.
  * 가변 데이터는 클라이언트에 의해 설정됨.
 
User Service API
===
* GET HTTP Method
  * `@RestController` 어노테이션이 있는 컨트롤러 클래스에서 `@GetMapping` 어노테이션을 통해 GET HTTP Method 구현
* POST HTTP Method
  * `@RestController` 어노테이션이 있는 컨트롤러 클래스에서 `@PostMapping` 어노테이션을 통해 POST HTTP Method 구현
  * `@RequestBody`: post/put HTTP Method에서 클라이언트로부터 JSON/XML 같은 오브젝트 형태의 데이터를 받기 위해 매개변수 형태로 전달인자와 함께 핸들러 함수에 정의
  * 같은 url을 사용하더라도 HTTP Method 방식에 따라 다르게 동작
* HTTP 상태 반환 제어
  * `ServletUriComponentsBuilder` 클래스를 통해 HTTP 상태코드, uri 변경 가능
  * 핸들러 함수에서 생성한 URI를 ResponseEntity에 담아 반환
  * `ResponseEntity`: 직접 HTTP body, headers, status code를 세팅하여 반환할 수 있는 클래스
  * 사용자를 추가할 때 id값은 서버에서 자동 생성하므로 클라이언트 측에서 id값을 알 수 없음. id값을 알기 위해선 서버에 다시 요청 해야됨. 따라서 POST Method의 실행결과로 생성된 id값을 확인 가능한 uri 반환해 네트워크 트래픽 감소
* HTTP Status Code 제어
  * 서버가 HTTP 요청을 처리할 수 없는 경우에 사용할 예외 처리 클래스를 생성하고 `@ResponseStatus` 어노테이션을 통해 Status code 전달함으로써 예외 처리
  * HTTP Status Code
    * 2XX -> OK
    * 4XX -> Client Error
    * 5XX -> Server Error
* AOP 사용 HTTP Status Code 제어
  * `@RestController`, `@ControllerAdvice` 어노테이션을 추가하고 ResponseEntityExceptionHandler을 상속받은 예외 처리 클래스를 생성해 모든 컨트롤러가 실행되기 전 해당 예외 처리 클래스가 실행될 수 있도록 AOP활용
  * `@ExceptionHandler` 어노테이션을 통해 해당 Exception handler가 처리할 Exception 종류 지정
  * `@ControllerAdvice`: @Controller나 @RestController에서 발생한 예외를 한 곳에서 관리하고 처리할 수 있게 도와주는 어노테이션
  * `@ExceptionHandler`: @Controller, @RestController가 적용된 Bean내에서 발생하는 예외를 잡아서 하나의 메서드에서 처리해주는 어노테이션
* DELETE HTTP Method
  * `@RestController` 어노테이션이 있는 컨트롤러 클래스에서 `@DeleteMapping` 어노테이션을 통해 DELETE HTTP Method 구현

RESTful Service
===
* Validation API
  * javax의 validation API와 Hibernate validation API를 사용해 유효성 검사 가능
  * 도메인 클래스에서 각 필드 데이터에 데이터의 범위를 지정할 수 있는 `@Size`, 과거 값만 유효한 데이터로 설정하는 `@Past`와 같은 validation 어노테이션을 추가해 유효 조건 설정
  * 컨트롤러 클래스 내 HTTP POST Method에서 파라미터 형태로 `@Valid` 어노테이션을 추가하면 입력 받는 객체에 대해 유효성 검사 가능
* 다국어 처리
  * 다국어 처리에 필요한 bean을 SpringBootApplication class에 등록해서 스프링부트가 초기화될 때 메모리에 등록되도록 함.
  * 구현 과정
    * application.yml에 다음 코드 추가
     ```
      spring:
         messages:
            basename: messages
     ```
    * `basename: 파일이름`: 다국어 파일 이름 설정
    * `resources/` 하위에 다국어 파일 생성
    * 컨트롤러 클래스 내 다국어 처리 메소드에서 `@RequestHeader` 어노테이션을 통해 Accept-Language의 value에 해당하는 다국어 받아 사용
* XML format
  * client가 GET 요청 시 header에 'Accept'의 value로 `application/xml`를 보내면 json형식이 아닌 xml형식으로 response를 전송함.
  * pom.xml에 다음 라이브러리를 추가하면 xml형식으로 응답 가능
  ```
  <dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.10.2</version>
  </dependency>
  ```
* Filtering
  * 도메인 클래스 내 특정한 필드 데이터를 외부에 노출시키지 않는 방법
  * 방법1
    * 도메인 클래스 각 필드에 `@JsonIgnore` 어노테이션을 추가해 해당 데이터 필드 노출 제어
    * 도메인 클래스에 `@JsonIgnoreProperties` 어노테이션을 추가해 해당 데이터 필드 노출 제어
      * ex) `@JsonIgnoreProperties(value={"password", "ssn"})`
  * 방법2
    1. 도메인 클래스에 `@JsonFilter("filter 명")` 어노테이션을 추가해 filter 명을 설정
    2. 컨트롤러 클래스 내 API 핸들러 함수 안에서 `SimpleBeanPropertyFilter` 객체를 선언하고 해당 객체의  `filterOutAllExcept` 함수를 통해 필터링하지 않을 필드를 설정.
    3. `FilterProvider` 객체를 `SimpleFilterProvider().addFilter("filter를 적용할 bean이름",)`하여 선언하고 `MappingJacsonValue` 객체에서 setFilters 함수를 통해 필터링 대상이 되는 bean 객체인 `FilterProvider` 객체를 적용시켜 반환해 필터링 할 수 있음.
* Versioning
  * URI
    * 컨트롤러 클래스에서 각 메소드에 매핑되는 uri를 버전에 따라 구분해 API 제공
    * `BeanUtils.copyProperties`: 스프링에서 기본으로 제공해주는 메소드로서 객체를 쉽고 간결하게 복사 가능
    * ex) `@GetMapping("/v1/users/{id}")`
  * Request Parameter
    * uri 뒤에 request parameter값을 전달함으로써 버전에 따른 API 제공
    * ex) `@GetMapping(value = "/users/{id}/", params = "version=1")`
    * request: https://localhost:8088/admin/user/{id}/?version={version}
  * header
    * header값을 이용해 버전에 따른 API 제공
    * ex) `@GetMapping(value = "/users/{id}/", headers="X-API-VERSION=1")`
    * request: https://localhost:8088/admin/users/{id}
    * header: X-API-VERSION={version}
  * MIME type
    * 이메일과 함께 전송되는 메일을 텍스트 문자로 전환해서 이메일 서버에 전달하기 위한 방법
    * 최근에는 웹을 통해서 여러가지 파일을 전달하기 위해 사용되는 일종의 파일 지정 형식
    * ex) `@GetMapping(value = "/users/{id}", produces = "application/vnd.company.appy1+json")`
    * request: https://localhost:8088/admin/users/{id}
    * header: Accept=application/vnd.company.appy{version}+json

Spring Boot API
===
* HATEOAS
  * 현재 리소스와 연관된(호출 가능한) 자원 상태 정보를 제공
  * 클라이언트가 서버로부터 어떠한 요청을 할 때, 요청에 필요한 URI를 응답에 포함시켜 반환
  * `pom.xml`에 hateoas 라이브러리 추가
  ```
  <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-hateoas</artifactId>
  </dependency>
  ```
  * spring 2.1.8 버전 이하: `Resource`/'ControllerLInkBuilder` 사용
  * spring 2.2 버전 이상: `EntityModel`/`WebMvcLinkBuilder` 사용
* Swagger
  * REST 웹 서비스에서 설계, 빌드, 문서화, 사용에 관련된 작업을 지원하는 오픈소스 프레임워크
  * 서버로 요청되는 URL 리스트를 HTML화면으로 문서화 및 테스트 할 수 있는 라이브러리
  * `pom.xml`에 dependency 추가
  ```
  <dependency>
   <groupId>io.springfox</groupId>
   <artifactId>springfox-boot-starter</artifactId>
   <version>3.0.0</version>
  </dependency>
  ```
  * http://localhost:8088/v2/api-docs: swagger file에 의해 만들어진 내용을 json 타입으로 나타냄
  * http://localhost:8088/swagger-ui/: html 형태로 보여짐
  * swagger의 documentation 커스터마이징
    * `@Configuration`어노테이션을 통해 설정이라는 것을 명시하고 `@@EnableSwagger2`어노테이션을 통해 Swagger2 버전 활성화
    * Swagger설정의 핵심이 되는 Bean인 Docket객체를 반환하는 메소드 생성, API 자체에 대한 스펙은 해당 컨트롤러에서 작성
    * Docket에 apiInfo(), produces(), consumes() 등의 설정을 할당해 커스트마이징 할 수 있음
    * 모델에서도 `@ApiModel`, `@ApiModelProperty` 어노테이션으로 모델/필드에 각각 설명을 추가해 커스터마이징 할 수 있음.
* Actuator
  * App의 상태를 모니터링하는 기능
  * `pom.xml`에 dependency 추가
  ```
  <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```
* HAL Browser
  * 어플리케이션의 부가적인 기능 부여
  * API resource들 사이에서 일관적인 하이퍼링크를 제공하는 방식
  * API 설계시 HAL을 도입하면 API 간에 쉽게 검색 가능
  * HAL을 API Response message에 적용하면 message의 형식(json/xml)에 상관없이 api를 쉽게 사용할 수 있는 부가적인 정보(메타 정보)를 하이퍼링크 형식으로 간단하게 포함할 수 있게 됨.
  * resource는 컴퓨터의 자원, 유저 정보를 변경, 수정, 추가한 작업을 의미, 이러한 resource를 외부에 공개하기 위해 RESTful 서비스를 사용함.
  * 해당하는 요청 작업에 부가적으로 사용할 수 있는 또 다른 resource를 연결해서 보여주기 위해 html의 링크 즉, 하이퍼 텍스트 기능을 사용함.
  * REST 자원을 표현하기 위한 자료구조를 그때 그때 생성하지 않더라도, HATEOAS 기능을 사용할 수 있음.
    * HATEOAS를 사용하는 경우 HATEOAS 정보를 추가하기 위해 매번 link 객체를 생성함. 
    * HAL은 필요한 resource를 매번 작업하지 않더라도 추가로 사용할 수 있는 link가 자동으로 설계됨. 
  * `pom.xml`에 dependency 추가
  ```
  <dependency>
   <groupId>org.springframework.data</groupId>
   <artifactId>spring-data-rest-hal-browser</artifactId>
   <version>3.3.2.RELEASE</version>
  </dependency>
  ```
  * http://localhost:8088/browser/index.html 에서 API 리소스들 조회 가능
* Security
  * passward를 통한 사용자 인증 및 접근 권한 처리
  * Spring Security
    * `pom.xml`에 아래 dependency를 추가하면 passward가 자동 생성되고, 이를 통해 웹서비스 호출 시 접근 권한 처리
    ```
    <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ```
  * Configuration
    * ID, password를 개발자가 직접 지정해 접근 권한 처리
    * `application.yml`에서 id, password 지정
    ```
    spring:
     security:
      user:
       name: username
       password: passw0rd
    ```
    * yml 파일에 id, password를 지정할 경우 변경할 때마다 서버를 재부팅해야됨. 따라서 DB에서 사용자 정보를 가져오는 방식으로 프로그램 구현 필요
    * `WebSecurityConfigurerAdapter`를 상속받아 사용자가 정의한 인증 정보를 추가할 수 있고, 서버의 재부팅 없이 id, password 변경 가능
    

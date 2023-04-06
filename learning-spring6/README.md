# Spring Framework
[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard) 정리 문서

# 검증
## Validator  
* 검증을 체계적으로 제공하기 위해 스프링에서 제공하는 인터페이스
* `supports() {}` : 해당 검증기가 지원하는 타입인지 확인
* `validate(Object target, Errors errors)` : 검증 대상 객체와 BindingResult

```java
public interface Validator {
    boolean supports(Class<?> clazz);
    validate(Object target, Errors errors);
}
```
**예시** : 입력된 ItemName, price, quantity를 검증하는 validator 구현 및 적용.  
1. validator 인터페이스 구현체 생성
```java
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        /*
            검증 로직
        */
    }
}
```
2. 컨트롤러에서 validator 구현체 호출
* WebDataBinder에 검증기를 추가하면 해당 컨트롤러에서 검증기를 자동 적용할 수 있다.
* @InitBinder는 해당 컨트롤러에만 영향을 준다.
```java
@InitBinder
public void init(WebDataBinder dataBinder) {
    log.info("init binder {}", dataBinder);
    dataBinder.addValidators(itemValidator);
}
```
3. @Validated 적용
* 검증 대상 앞에 `@Validated`를 붙인다. (검증기를 실행하라는 애노테이션)
* 해당 애노테이션이 붙으면 앞서 WebDataBinder에 등록한 검증기를 찾아서 실행한다.
* 여러 검증기가 등록되어 있는 경우 어떤 검증기가 실행되어야 할지 `supports()`를 통해 판단한다.
* 여기서는 `supports(Item.class)`가 호출되고, 결과가 true인 ItemValidator의 validate()가 호출된다.
```java
@PostMapping("/add")
public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult) {
    
    if (bindingResult.hasErrors()) {
        log.info("errors={}", bindingResult);
        return "validation/v2/addForm";
    }
}
```
> 참고 
> * 검증시 `@Validated`, `@Valid` 둘 다 사용가능 하다.
> * @Validated는 스프링 전용 검증 애노테이션이다.
> * @Valid는 자바 표준 검증 애노테이션이다. 사용하려면 아래 의존관계를 추가해줘야 한다.
> ```java
> implementation 'org.springframework.boot:spring-boot-starter-validation'
> ```

## Bean Validation
* 검증 애노테이션과 여러 인터페이스의 모음으로 Bean Validation 2.0(JSR-380)이라는 기술의 표준
* Bean Validation을 구현한 기술중에 일반적으로 사용하는 구현체는 하이버네이트 Validator이다.
  * jakarta.validation-api: Bean Validation 인터페이스
  * hibernate-validator: 구현체

### Bean Validation - 순수
스프링과 통합하지 않고 순수한 Bean Validation을 사용해보자.  
1. 의존관계 추가 
* 의존관계를 추가하면 인터페이스와 구현체 라이브러리가 추가된다.
```java
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
2. 모델에 필드 검증 애노테이션 추가
```java
@Data
public class Item {

      private Long id;

      @NotBlank(message = "공백 X")
      private String itemName;
      
      @NotNull
      @Range(min = 1000, max = 1000000)
      private Integer price;
      
      @NotNull
      @Max(9999)
      private Integer quantity;
      
      public Item() {}
      
      public Item(String itemName, Integer price, Integer quantity) { 
          this.itemName = itemName;
          this.price = price;
          this.quantity = quantity;
      }     
}
```
3. 테스트 코드
```java
public class BeanValidationTest {

    @Test
    void beanValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setItemName(" ");  // 공백
        item.setPrice(0);
        item.setQuantity(10000);

        // violations가 빈 값이면 오류가 없는 것, 아닌 경우 오류가 있는 것
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation = " + violation.getMessage());
        }
    }
}
```
### Bean Validation - 스프링 적용
* 스프링 부트에 `spring-boot-starter-validation` 라이브러리를 넣으면 자동으로 Bean Validator를 인지하고 스프링에 통합한다.
* 스프링 부트는 LocalValidatorFactoryBean을 자동으로 글로벌 Validator로 등록
* 따라서, 컨트롤러마다 Validator 구현체를 호출하지 않고, `@Valid` 또는 `@Validated`만 붙여 검증기 사용 가능
* `주의` : 글로벌 Validator를 직접 등록하면 스프링 부트는 Bean Validator를 글로벌 Validator로 등록하지 않는다.

**예시**
* 앞서 정의한 item 객체의 각 필드 검증
* 검증 대상 앞에 @Validated 또는 @Valid 붙여 검증기 실행
```java
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;
    
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v3/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }
}
```
검증 순서
1. @ModelAttribute 각각의 필드에 타입 변환 시도
2. 성공하면 다음으로, 실패하면 typeMismatch로 FieldError 추가
3. Validator 적용
> 모델 객체 각 필드에 맞는 타입의 값이 들어와야 검증도 의미가 있으므로,  
> @ModelAttribute로 받은 객체에서 타입 변환 시도에 성공한 필드만 Bean Validation 적용

### Bean Validation - 에러 코드
* Bean Validation이 기본으로 제공하는 오류 메시지를 더 자세히 변경하고 싶으면 어떻게 할까?
* Bean Validation을 적용하면 bindingResult에 애노테이션 이름으로 검증 오류 코드가 등록된
* 이 오류 코드를 기반으로 `MessageCodesResolver`에 의해 다양한 메시지 코드가 순서대로 실행된다.

@NotBlank 메시지 코드 우선순위
* NotBlank.item.itemName
* NotBlank.itemName
* NotBlank.java.lang.String
* NotBlank

**예시**
1. 메시지 등록 (errors.properties)
```yml
#Bean Validation 추가

# 이게 1 순위
NotBlank.item.itemName=상품 이름을 적어주세요.

NotBlank={0} 공백은 절대 안됨
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```
2. 실행해보면 등록한 메시지가 정상 적용되는 것을 확인할 수 있다.

Bean Validation 메시지 찾는 우선순위
1. 생성된 메시지 코드 순서대로 `messageSource` 에서 메시지 찾기
2. 애노테이션의 message 속성 사용 : @NotBlank(message = "공백 X")
3. 라이브러리가 제공하는 기본 값 사용 : 공백일 수 없습니다.

### Bean Validation - 한계
* 모델을 등록할 때와 수정할 때 검증 조건이 다를 수 있다.
* 예시로, Item을 등록할 때는 수량을 9999까지 등록 가능하지만, 수정할 때는 무제한으로 변경가능한 경우
* `해결 방법` : 객체 분리

**객체 분리**
* 컨트롤러마다 전달받는 데이터가 다르고, 검증 조건이 다르기 때문에 객체를 분리하는 것이 좋다.
* ItemSaveForm
```java
@Data
public class ItemSaveForm {
    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;
}
```
* ItemUpdateForm
```java
@Data
public class ItemUpdateForm {
    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    private Integer quantity;
}
```
> `TIP` : [검증 애노테이션 모음](https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-defineconstraints-spec) 을 활용해 다양한 검증 애노테이션을 적용해보자!

### Bean Validation - HTTP 메시지 컨버터
* @Valid, @Validated 는 `HttpMessageConverter(@RequestBody)`에도 적용할 수 있다.
* `@ModelAttribute`: HTTP 요청 파라미터 (URL 쿼리 스트링, POST Form)를 다룰 때 사용
* `@RequestBody`: HTTP Body의 데이터를 객체로 변환할 때 사용, 주로 API JSON 요청 다룰 때 사용


**예시**
* ValidationItemApiController
```java
@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
```
**@ModelAttribute vs @RequestBody**
* @ModelAttribute
  * 각각의 필드 단위로 세밀하게 적용된다. 
  * 특정 필드에 타입이 맞지 않는 오류가 발생해도 나머지 필드는 정상 처리
* @RequestBody
  * HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생한다.
  * 만약 Integer 타입 price에 문자값을 넣어 JSON 데이터를 넘겨준 경우 예외가 발생해 컨트롤러도 호출되지 않고, Validator도 적용할 수 없다.

# 로그인 처리
## Cookie
* 로그인 상태를 유지하기 위해 Cookie를 사용할 수 있다.
* `영속 쿠키` : 만료 날짜를 입력하면 해당 날짜까지 유지
* `세션 쿠키` : 만료 날짜를 생략하면 브라우저 종료시까지만 유지


**cookie를 사용한 로그인 처리 과정**
* 서버에서 로그인에 성공하면 쿠키를 생성하고, `HttpServletResponse`에 담아 브라우저에 전달한다.
* 이후 브라우저는 모든 요청에 쿠키를 담아 요청한다.

1. 서버에서 쿠키 생성해 브라우저에 전달
<img src="https://user-images.githubusercontent.com/50009240/228516047-4533e9f2-8fc9-4519-8052-d1c625fd7860.png" width="500" height="250">

2. 클라이언트는 모든 요청에 쿠키 담아 요청
<img src="https://user-images.githubusercontent.com/50009240/228516533-22c70066-0516-44c3-8d6c-0c95dfd255b3.png" width="500" height="250">

**로그인 - 예시**
* LoginService
```java
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
```
* LoginController
  * 로그인 성공 시 Cookie를 생성하고, HttpServletResponse에 넣어 브라우저에 전달
```java
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리

        // 쿠키에 시간 정보를 주지 않으면 세션 쿠키로 설정된다.
        // - 브라우저 종료시 모두 종료
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }
    
    // ...
}
```
* HomeController
```java
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    // 쿠키를 갖고있는 사용자 id만 들어오도록 required = false 설정
    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
```

**로그아웃 - 예시**
* LoginController
  * 쿠키의 만료날자(maxAge)를 0으로 세팅해 로그아웃 처리
```java
@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // ... 로그인 api

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
```
**쿠키 보안 문제**
* 문제점
  * 브라우저에서 쿠키 값을 변경할 수 있다.
  * 클라이언트의 쿠키에 보관된 정보가 해킹될 수 있음.
* 대안
  * 쿠키에 중요한 값을 노출하지 않고 예측 부가능한 임의의 토큰(랜덤 값)을 노출해 서버에서 토큰과 사용자 id 매핑해서 인식
  * 토큰을 털어가도 시간이 지나면 사용할 수 없도록 토큰 만료시간을 짧게 유지

## Session
* 서버에 중요한 정보를 보관하고 추정 불가능한 임의의 식별자로 클라이언트와 서버의 연결을 유지하는 방법이다.
* 중요한 포인트는 회원과 관련된 중요한 정보는 클라이언트에 전달하지 않는다는 것이다.
* 오직 추정 불가능한 세션 ID만 쿠키를 통해 클라이언트에 전달한다.

**Session 동작 방식**
* 서버에서 추정 불가능한 UUID로 세션 ID를 생성하고, 세션 ID와 세션에 보관할 값을 서버의 세션 저장소에 보관한다.
* 그 후, 세션ID만 쿠키에 담아 브라우저에 전달하는 방식이다.

1. 서버에서 세션 ID 생성 후 회원 정보는 세션 저장소에 저장하고, 세션ID를 쿠키에 담아 브라우저에 전달
<img src="https://user-images.githubusercontent.com/50009240/228537842-a8daf28d-a8f1-4ebe-966b-bc1c39383a5d.png" width="500" height="250">

2. 클라이언트는 요청시 항상 세션ID가 담긴 쿠키를 전달
<img src="https://user-images.githubusercontent.com/50009240/228538061-d672113c-8f46-4895-a6b5-3fffd550730e.png" width="500" height="250">

### Session - HttpSession
**로그인 - 예시**
* SessionConst
  * `HttpSession`에 데이터를 보관하고 조회할 때, 같은 이름이 중복 되어 사용되므로, 상수 하나 정의했다.
```java
public class SessionConst {
    public static final String LOGIN_MEMBER = "loginMember";
}
```
* LoginController
```java
@PostMapping("/login")
public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
    if (bindingResult.hasErrors()) {
        return "login/loginForm";
    }

    Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

    if (loginMember == null) {
        bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
        return "login/loginForm";
    }

    // 로그인 성공 처리

    // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
    HttpSession session = request.getSession();
    // 세션에 로그인 회원 정보 보관
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

    return "redirect:/";
}
```
* request.getSession(true)
  * 세션이 있으면 기존 세션 반환
  * 세션이 없으면 새로운 세션 생성해 반환
  * true가 default
* request.getSession(false)
  * 세션이 있으면 기존 세션을 반환
  * 세션이 없으면 새로운 세션을 생성하지 않는다. null을 반환한다.
* session.setAttribute()
  * 세션에 데이터 보관 (key, value 형식)
  * 하나의 세션에 여러 값을 저장할 수 있다. 메모리에 저장됨

**로그아웃 - 예시**
* LoginController
```java
@PostMapping("/logout")
public String logoutV3(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }
    return "redirect:/";
}
```
* request.getSession(false) : 세션을 갖고올 때, 세션이 없으면 null을 반환하도록 설정
* session.invalidate() : 해당 세션 및 데이터를 다 날린다.

**@SessionAttribute**
* 더 편리하게 이미 로그인된 사용자를 찾기 위해 스프링에서 제공하는 어노테이션
<br></br>
* @SessionAttribute 사용하지 않은 방법
  * HttpServletRequest에서 session을 갖고오고, 해당 세션에 데이터 존재여부를 확인해야 됨
```java
@GetMapping("/")
public String homeLoginV3(HttpServletRequest request, Model model) {
    
    //세션이 없으면 home
    HttpSession session = request.getSession(false); 
    
    if (session == null) {
        return "home";
    }
    
    Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
    //세션에 회원 데이터가 없으면 home 
    
    if (loginMember == null) {
        return "home";
    }
    
    //세션이 유지되면 로그인으로 이동 
    model.addAttribute("member", loginMember); 
    return "loginHome";
}
```
* @SessionAttribute 사용한 방법
```java
@GetMapping("/")
public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    
    //세션에 회원 데이터가 없으면 home 
    if (loginMember == null) {
          return "home";
    }
      
    //세션이 유지되면 로그인으로 이동 
    model.addAttribute("member", loginMember); 
    return "loginHome";
}
```

**세션 정보**
* sessionId: 세션Id, JSESSIONID 의 값
* maxInactiveInterval: 세션의 유효 시간
* creationTime: 세션 생성일시
* lastAccessedTime: 세션과 연결된 사용자가 최근에 서버에 접근한 시간, 클라이언트에서 서버로 sessionId(JSESSIONID)를 요청한 경우에 갱신
* isNew: 새로 생성된 세션인지 아니면 이미 과거에 만들어졌고, 클라이언트에서 서버로 sessionId (JSESSIONID)를 요청해서 조회된 세션인지 여부
<br></br>
* SessionInfoController
```java 
@Slf4j
@RestController
public class SessionInfoController {

     @GetMapping("/session-info")
     public String sessionInfo(HttpServletRequest request) {
         HttpSession session = request.getSession(false); 
         
         if (session == null) {
             return "세션이 없습니다."; 
        }
        
        //세션 데이터 출력 
        session.getAttributeNames().asIterator()
        .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));
        
        log.info("sessionId={}", session.getId()); 
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval()); 
        log.info("creationTime={}", new Date(session.getCreationTime())); 
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime())); 
        log.info("isNew={}", session.isNew());
        return "세션 출력"; 
    }
}
```

**타임 아웃 설정**
* HttpSession은 세션 생성 시점이 아니라 사용자가 서버에 최근에 요청한 시간을 기준으로 타임 아웃이 동작
* 세션 삭제: session.invalidate() 가 호출 되는 경우에 삭제됨
* 세션 타임 설정
  * 프로퍼티 값 수정을 통한 글로벌 세션 타임 아웃 설정: server.servlet.session.timeout=60
  * 코드에서 HttpSession 수정을 통한 세션 별 타임 아웃 설정: session.setMaxInactiveInterval(1800);

## 서블릿 필터
**서블릿 필터 소개**
* 필터는 서블릿이 지원하는 수문장
* 상품 관리 컨트롤러에서 로그인 여부를 체크하는 로직을 하나하나 작성하는 것은 매우 불편하므로 애플리케이션 여러 로직에서 공통으로 관심이 있는 있는 것을 공통 관심사로 처리하는 것이 좋음
* 스프링의 AOP로도 공통 관심사를 해결할 수 잇지만, 웹과 관련된 공통 관심사는 지금부터 설명할 서블릿 필터 또는 스프링 인터셉터를 사용하는 것이 좋음
* 서블릿 필터나 스프링 인터셉터는 HttpServletRequest를 제공
* 필터 흐름
  * HTTP 요청 ->WAS-> 필터 -> 서블릿(디스패처 서블릿) -> 컨트롤러
* 필터 제한
  * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러 // 로그인 사용자
  * HTTP 요청 -> WAS -> 필터(적절하지 않은 요청이라 판단, 서블릿 호출X) // 비 로그인 사용자
* 필터 체인
  * HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러

**필터 인터페이스**
* was에서 doFilter를 호출해 필터를 적용하고, 모든 필터가 통과하면 서블릿을 호출함
* 필터 인터페이스를 구현하고 등록하면, 서블릿 컨테이너가 필터를 `싱글톤` 객체로 생성하고, 관리한다.
```java
public interface Filter {
      public default void init(FilterConfig filterConfig) throws ServletException
  {}
      public void doFilter(ServletRequest request, ServletResponse response,
              FilterChain chain) throws IOException, ServletException;
      public default void destroy() {}
}
```
* init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출
* doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출, 필터의 로직을 구현
* destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출

**서블릿 필터 - 요청 로그**
* 모든 요청을 로그로 남기는 필터를 구현해보자


LogFilter
```java
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;  // HTTP 사용 시 다운케스팅 필요
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
```
* 필터를 사용하려면 Filter 인터페이스를 구현해야 한다.
* HTTP 요청이 오면 `doFilter`가 호출된다.
* HTTP 요청을 구분하기 위해 요청당 임의의 uuid 생성
* `chain.doFilter(request, response);` : 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출한다. (중요)
  * 이 코드가 없다면, 다음단계로 진행되지 않는다.

WebConfig
```java
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return filterFilterRegistrationBean;
    }
}
```
* 스프링 부트에선 `FilterRegistrationBean`을 사용해 필터를 등록하면 된다.
* `setFilter(new LogFilter())` : 등록할 필터 지정
* `setOrder(1)` : 필터를 체인으로 동작한다. 이를 통해 순서를 지정할 수 있다. 낮을 수록 먼저 동작한다.
* `addUrlPatterns("/*")` : 필터를 적용할 URL 패턴 지정, 한번에 여러 패턴 지정 가능

> `TIP` : 실무에서 HTTP 요청시 같은 요청의 로그에 모두 같은 식별자를 자동으로 남기는 방법은 logback mdc로 검색해보자.

**서블릿 필터 - 인증 체크**
* 로그인 되지 않은 사용자는 상품 관련 페이지에 접근할 수 없도록 인증 체크 필터를 개발해보자

LoginCheckFilter
```java
@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse)  response;

        try {
            log.info("인증 체크 필터 시작{}", requestURI);

            // 화이트 리스트를 제외한 모든 경우에 인증 체크 로직
            if (!isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;  // 예외 로깅 가능하지만, 톰켓까지 예외를 보내주어야 함
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
```
* whitelist가 아닌 url로 요청이 들어오면, 로그인 사용자인지 세션을 확인하고 세션이 없다면 로그인 화면으로 redirect
* 이때 redirectURL에는 사용자가 요청한 url이 들어간다 ex) `http://localhost:8080/login?redirectURL=/items`

WebConfig
```java
@Bean
public FilterRegistrationBean loginCheckFilter() {
    FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
    filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
    filterFilterRegistrationBean.setOrder(2);
    filterFilterRegistrationBean.addUrlPatterns("/*");

    return filterFilterRegistrationBean;
}
```

LoginController
```java
@PostMapping("/login")
public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, 
                      @RequestParam(defaultValue = "/") String redirectURL,
                      HttpServletRequest request
) {
    if (bindingResult.hasErrors()) {
        return "login/loginForm";
    }

    Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

    if (loginMember == null) {
        bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
        return "login/loginForm";
    }

    // 로그인 성공 처리

    // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
    HttpSession session = request.getSession();
    // 세션에 로그인 회원 정보 보관
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

    // redirectURL 적용
    return "redirect:" + redirectURL;
}
```
* LoginCheckFilter에서 미인증 사용자는 요청 경로를 포함해 `/login`에 `redirectURL` 요청 파라미터를 추가해서 요청했다.
* 이 값을 사용해서 로그인 성공시 해당 경로로 고객을 `redirect` 한다.

## 스프링 인터셉터
* 스프링 인터셉터는 `스프링 MVC`가 제공하는 기술이다.
* 서블릿 필터와 같이 웹과 관련된 공통 관심 사항을 효과적으로 해결할 수 있는 기술
* 스프링 인터셉터는 디스패처 서블릿(MVC의 시작점)과 컨트롤러 사이에서 컨트롤러 호출 직전에 호출된다.
* 스프링 인터셉터 흐름
  * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
* 스프링 인터셉터 제한
  * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러  // 로그인 사용자
  * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 (적절하지 않은 요청이라 판단, 컨트롤러 호출 X)  // 비 로그인 사용자
* 스프링 인터셉터 체인
  * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터1 -> 인터셉터2 -> 컨트롤러

**스프링 인터셉터 인터페이스**
```java
public interface HandlerInterceptor {
    
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {}

    default void postHandle(HttpServletRequest request, HttpServletResponse  response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {}

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {}
}
```
* `preHandle` : 컨트롤러 호출 전에 호출
* `postHandle` : 컨트롤러 호출 후에 호출
* `afterCompletion` : 뷰가 렌더링된 이후에 호출
* 서블릿 필터는 doFilter() 하나만 제공된다. 인터셉터는 preHandle, postHandle, afterCompletion과 같이 단계적으로 잘 세분화되어 있다.
* 서블릿 필터의 경우 단순히 request, response만 제공했지만, 인터셉터는 어떤 컨트롤러(handler)가 호출되는지 호출 정보도 받을 수 있다. 그리고 어떤 modelAndView가 반환되는지 응답 정보도 받을 수 있다.

**스프링 인터셉터 호출 흐름**

<img src="https://user-images.githubusercontent.com/50009240/229864998-858dc5ab-4361-494a-aa82-e1e2213ec415.png"
 width="670" height="350">

**스프링 인터셉터 예외**
* `preHandle` : 컨트롤러 호출 전에 호출되므로, 컨트롤러 예외와 무관
* `postHandle` : 컨트롤러에서 예외가 발생하면 호출되지 않는다.
* `afterCompletion` : 항상 호출된다. 컨트롤러 예외가 발생해도 호출된다.

**스프링 인터셉터 - 요청 로그**
* 모든 요청을 로그로 남기는 스프링 인터셉터를 개발해보자

LogInterceptor
```java
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        // 스프링 인터셉터는 호출 시점이 완전히 분리되어 있으므로, request에 담아서 넘겨줘야함
        request.setAttribute(LOG_ID, uuid);  

        // @RequestMapping: HandlerMethod
        // 정적 리소스 : ResourceHttpRequestHandler
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;  // 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        if (ex != null) {
            log.error("aftercompletion error!!", ex);
        }
    }
}
```
* 핸들러 정보는 어떤 핸들러 매핑을 사용하는가에 따라 달라진다.
* 스프링을 사용하면 일반적으로 `@Controller`, `@RequestMapping`을 활용한 핸들러 매핑을 사용하는데, 이 경우 핸들러 정보로 `HandlerMethod`가 넘어온다.

WebConfig
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");
    }
}
```
* WebMvcConfigurer가 제공하는 addInterceptors() 를 사용해서 인터셉터를 등록할 수 있다.
* `registry.addInterceptor(new LogInterceptor())` : 인터셉터 등록
* `order(1)` : 인터셉터의 호출 순서 지정. 낮을 수록 먼저 호출된다.
* `addPathPatterns("/**")` : 인터셉터를 적용할 URL 패턴을 지정한다.
* `excludePathPatterns("/css/**", "/*.ico", "/error")` : 인터셉터에서 제외할 패턴을 지정한다.

**스프링 인터셉터 - 인증 체크**
* 로그인되지 않은 사용자는 상품 관련 페이지에 접근할 수 없도록 인증 체크 인터셉터를 개발해보자

LoginCheckInterceptor
```java
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            response.sendRedirect("/login?redirectURL" + requestURI);
            return false;
        }

        return true;
    }
}
```
WebConfig
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error");
    }
}
```
* 서블릿 필터는 적용하지 않을 경로를 whitelist로 지정해 요청 경로가 whitelist에 포함되는지 확인 후 적용했다.
* 그에 반해, 인터셉터는 적용하거나 하지 않을 부분을 `addPathPatterns`, `excludePathPatterns`에 작성하면 된다.

> `정리`
> * 서블릿 필터와 스프링 인터셉터는 웹과 관련된 공통 관심사를 해결하기 위한 기술이다.
> * 서블릿 필터와 비교해서 스프링 인터셉터가 개발자 입장에서 훨씬 편리하다.
> * 특별한 문제가 없다면 인터셉터를 사용하는 것이 좋다.

### ArgumentResolve - 인증 체크
* ArgumentResolve를 활용하여 로그인 회원을 조금 더 편리하게 찾아보자.

HomeController
```java
@GetMapping("/")
public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

    if (loginMember == null) {
        return "home";
    }

    // 세션이 유지되면 로그아웃 버튼이 있는 home으로 이동
    model.addAttribute("member", loginMember);
    return "loginHome";
}
```
@Login 어노테이션
```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

}
```
* `@Target(ElementType.PARAMETER)` : 파라미터에만 사용
* `@Retention(RetentionPolicy.RUNTIME)` : 리플렉션 등을 활용할 수 있도록 런타임까지 애노테이션 정보가 남아있음

LoginMemberArgumentResolver
```java
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);  // @Login 어노테이션을 갖고 있는지
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());  // Member 타입의 파라미터인지

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        // 로그인된 사용자면 Member 반환
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
```
* `supportsParameter()` : @Login 애노테이션이 있으면서 Member 타입이면 해당 ArgumentResolver가 사용된다.
* `resolveArgument()` : 컨트롤러 호출 직전에 호출 되어서 필요한 파라미터 정보를 생성해준다
  * 여기서는 세션에 있는 로그인 회원 정보인 member 객체를 찾아 반환해준다.
  * 이후 컨트롤러의 메서드를 호출하면서 여기에서 반환된 member 객체를 파라미터에 전달해준다.

WebConfig
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // LoginMemberArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver()); 
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) 
                .order(1)
                .addPathPatterns("/**") 
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor()) 
                .order(2)
                 .addPathPatterns("/**")
                 .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error");
    }
}
```
* 기존에는 `@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)` 를 통해 로그인 회원 정보를 가져왔다.
* 이와 결과는 동일하지만 ArgumentResolver를 활용하면 더 편리하게 로그인 회원 정보를 조회할 수 있다.

# 예외처리와 오류 페이지
## 서블릿 예외 처리
* 자바
  * 자바의 메인 메서드를 직접 실행하는 경우 `main` 이라는 이름의 쓰레드가 실행된다.
  * 실행 도중 예외를 잡지 못하고, 실행한 `main()` 메서드를 넘어서 예외가 던져지면, 예외 정보를 남기고 해당 쓰레드는 종료된다.
* 웹 애플리케이션
  * 사용자 요청 별로 `별도의 쓰레드가 할당되고`, 서블릿 컨테이너 안에서 실행된다.
  * 애플리케이션에서 예외를 잡지 못하고, 서블릿 밖으로 예외가 전달되면 `500` error가 발생한다.
  * WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 -> 컨트롤러(예외발생)

**서블릿 예외 처리 - response.sendError()**
* HttpServletResponse가 제공하는 sendError 라는 메서드를 사용해 서블릿 컨테이너에 오류가 발생했다는 것을 전달할 수 있다.
* 이 메서드를 사용하면 HTTP 상태 코드와 오류 메시지도 추가할 수 있다.
* WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(response.sendError())

ServletExController
```java
@Slf4j
@Controller
public class ServletExController {

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
```

**서블릿 예외 처리 - 오류 페이지 등록**
* 예외 발생과 오류 페이지 요청 흐름
  * 애플리케이션에서 예외가 발생해서 WAS까지 예외가 전파된다.
  * WAS는 오류 페이지 경로를 찾아서 내부 오류 페이지를 호출한다. 이때 필터, 서블릿, 인터셉터, 컨트롤러가 모두 다시 호출된다.
* 예외 발생 : `WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)`
* 오류 페이지 요청 : `WAS (/error-page/500) 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500) -> View`

WebServerCustomizer
```java
@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
```
ErrorPageController
```java
@Controller
public class ErrorPageController {

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        return "error-page/500";
    }
}
```
<details>
<summary>404.html</summary>
<div markdown="1">

```bash
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container" style="max-width: 600px">
    <div class="py-5 text-center">
        <h2>404 오류 화면</h2>
    </div>
    <div>
        <p>오류 화면 입니다.</p>
    </div>
    <hr class="my-4">
</div> <!-- /container -->
</body>
</html>
```

</div>
</details>

<details>
<summary>500.html</summary>
<div markdown="1">

```bash
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container" style="max-width: 600px">
    <div class="py-5 text-center">
        <h2>500 오류 화면</h2></div>
    <div>
        <p>오류 화면 입니다.</p>
    </div>
    <hr class="my-4">
</div> <!-- /container -->
</body>
</html>
```

</div>
</details>

**서블릿 예외 처리 - 필터**
* 예외 발생과 오류 페이지 요청 문제점
  * 예외가 발생하면 WAS는 다시 오류 페이지를 호출한다. 이때 필터, 서블릿, 인터셉터, 컨트롤러가 모두 다시 호출된다.
  * 오류 페이지를 호출한다고, 필터나 인터셉터를 한번 더 호출하는 것은 매우 비효율적
  * 따라서, 클라이언트로부터 발생한 정상 요청인지, 아니면 오류 페이지 출력을 위한 내부 요청인지 구분하여 중복 호출을 방지해야한다.
  * 서블릿 필터는 중복 호출을 방지하기 위해 `DispatcherType` 이라는 추가 정보를 제공한다.
* `DispatcherType` : 필터가 제공하는 요청의 종류
  * REQUEST(기본값): 클라이언트 요청
  * ERROR: 오류 요청
  * FORWARD, INCLUDE, ASYNC 

WebConfig
* 필터는 `setDispatcherTypes()`를 통해 필터를 거치는 요청의 종류를 설정할 수 있다.
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        return filterFilterRegistrationBean;
    }
}
```
* 위 예제에서는 REQUEST 요청과, 오류 페이지 출력을 위한 ERROR 요청 모두 필터를 거치도록 설정해봤다.
* 기본값은 DispatcherType.REQUEST 이다. 별다른 설정이 없어도 오류 페이지 요청 시 필터를 거치지 않는다.

**서블릿 예외 처리 - 인터셉터**
* 인터셉터는 `excludePathPatterns("/error-page/**")` 을 통해 오류 페이지 요청 시 중복 호출 제거
WebConfig
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**");  // 오류 페이지 경로
    }
}
```

**스프링 부트 오류 페이지**
* 기본 에러 페이지
  * 스프링 부트는 ErrorPageController와 ErrorPage를 추가할 필요없이 기본 에러 페이지를 제공한다.
  * ErrorPage를 자동으로 등록한다. 이때, `/error` 라는 경로로 기본 오류 페이지를 설정한다.
  * `BasicErrorController` 라는 오류페이지 처리를 위한 스프링 컨트롤러를 자동으로 등록한다.
* 오류 페이지 등록
  * 개발자는 오류 페이지 화면만 `BasicErrorController`가 제공하는 룰과 우선순위에 따라 등록
  * 정적 html이면 정적 리소스, 뷰 템플럿을 사용해서 동적으로 오류 화면을 만들고 싶으면 뷰 템플릿 경로에 오류 페이지 파일을 만들어 넣어두기만 하면 된다.

> `참고`
> * 스프링 부트가 제공하는 `BasicErrorController`는 HTML 페이지를 제공하는 경우에 매우 편리 (4XX, 5XX 등 처리)
> * API 예외는 API 마다, 각각의 컨트롤러나 예외마다 서로 다른 응답 결과를 출력해야 할 때가 많으므로, 보통 `@ExceptionHandler` 사용

## API 예외 처리
### HandlerExceptionResolver (ExceptionResolver)
* 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하기 위해 사용
* 줄여서 `ExceptionResolver`라 한다.
* HandlerExceptionResolver 사용 이유
  * 예외가 발생해서 서블릿을 넘어 WAS까지 예외가 전달되면 HTTP 상태코드가 500으로 처리
  * 발생하는 예외에 따라서 400, 404 등등 다른 상태코드로 처리하는 필요성 존재
  * 이를 처리하기 위해 `HandlerExceptionResolver` 사용

**ExceptionResolver 처리 과정**
* ExceptionResolver를 적용하기 전에는 컨트롤러에서 발생한 예외가 WAS까지 그대로 전해졌다.
* ExceptionResolver를 적용하면, 예외 해결을 시도하고, 예외가 해결된 경우 WAS에 정상 응답을 전달한다.
* `참고` : ExceptionResolver로 예외를 해결해도 `postHandle()`은 호출되지 않는다.

<img src="https://user-images.githubusercontent.com/50009240/230442433-f2f2484e-4bb1-4c43-b8fe-d46a0242e252.png" widht="670" height="350">

**HandlerExceptionResolver - 인터페이스**
```java
public interface HandlerExceptionResolver {
    ModelAndView resolveException(
        HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex
    );
}
```
* `handler`: 핸들러(컨트롤러 정보)
* `Exception ex`: 핸들러(컨트롤러)에서 발생한 예외

**예시**
* MyHandlerExceptionResolver
```java
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}
```
* IllegalArgumentException 예외가 발생하면 response.sendError(400) 를 호출해서 HTTP 상태 코드를 400으로 지정하고, 빈 ModelAndView 를 반환하는 예시
* 이를 통해 서블릿에 정상응답이 전달되고, 500 에러가 아닌 400 에러로 처리할 수 있다.

**HandlerExceptionResolver 의 반환 값에 따른 DispatcherServlet 의 동작 방식**
* `빈 ModelAndView`: 뷰를 렌더링하지 않고, 정상 흐름으로 서블릿이 리턴
* `ModelAndView 지정`: ModelAndView에 View, Model 등의 정보를 지정해서 반환하면 뷰를 렌더링
* `null`: 다음 Exceptionresolver를 찾아서 실행, 처리할 수 있는 ExceptionResolver가 없으면 예외처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다 (500 에러로 처리)

**HandlerExceptionResolver 활용**
* 예외 상태 코드 변환
  * 예외를 `response.sendError(xxx)` 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임 
  * 이후 WAS는 서블릿 오류 페이지를 찾아 내부 호출, 예를 들어 스프링 부트가 기본으로 설정한 `/error` 호출 (위 예시 참고)
* 뷰 템플릿 처리
  * ModelAndView에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링해서 고객에게 제공
* API 응답 처리
  * response.getWriter().println("hello") 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는 것도 가능. JSON으로 응답하면 API 응답 처리를 할 수 있다.

**ExceptionResolver 등록**
* `WebMVCConfigurer` 인터페이스를 오버라이딩해 등록
* `extendHandlerExceptionResolvers`를 사용해 ExceptionResolver 등록
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
    }
}
```

### API 예외 처리 - HandlerExceptionResolver
* 예외가 발생하면 WAS까지 예외가 던져지고, WAS에서 오류 페이지 `/error`를 호출하는 과정은 너무 복잡하다.
* ExceptionResolver를 활용하면 예외를 깔끔하게 처리할 수 있다.
* HTTP 요청 헤더의 ACCEPT 값이 `application/json`이면 JSON으로 오류를 내려주고,
* 그 외 경우에는 (TEXT/HTML) error/500에 있는 HTML 오류 페이지를 보여주도록 구현해보자.

UserException
```java
public class UserException extends RuntimeException {
    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }
}
```
ApiExceptionController 
```java
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        
        // 추가
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }
}
```
UserHandlerExceptionResolver
```java
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if ("application/json".equals(acceptHeader)) {
                    // JSON
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);

                    return new ModelAndView();
                } else {
                    // TEXT/HTML
                    return new ModelAndView("error-page/500");
                }
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
```
WebConfig
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }
}
```

**결과**
* ACCEPT에 `application/json`을 넣어 요청을 보낸 경우 아래와 같이 응답이 온다.
```json
{
    "ex": "hello.exception.exception.UserException",
    "message": "사용자 오류"
}
```
* 이외 경우에는 html로 응답이 온다.

**정리**
* `ExceptionResolver`를 사용하면 컨트롤러에서 예외가 발생해도, 서블릿 컨테이너까지 예외가 전달되지 않고 ExceptionResolver에서 예외를 처리한다.
* 예외 처리를 하지 않거나, response.sendError()을 사용해 서블릿 컨테이너까지 예외가 올라가면 오류 페이지 호출을 위해 복잡한 프로세스가 실행된다.
* `ExceptionResolver`를 사용함으로써 예외처리가 깔끔해졌다.

### 스프링이 제공하는 ExceptionHandler








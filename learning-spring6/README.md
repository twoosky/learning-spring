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

1. 서버에서 세션 ID 생성 후 중요한 정보는 세션 저장소에 저장하고, 세션ID를 쿠키에 담아 브라우저에 전달
<img src="https://user-images.githubusercontent.com/50009240/228537842-a8daf28d-a8f1-4ebe-966b-bc1c39383a5d.png" width="500" height="250">

2. 클라이언트는 요청시 항상 세션ID가 담긴 쿠키를 전달
<img src="https://user-images.githubusercontent.com/50009240/228538061-d672113c-8f46-4895-a6b5-3fffd550730e.png" width="500" height="250">

























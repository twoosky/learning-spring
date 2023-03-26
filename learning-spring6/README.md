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

@NotBlank 메시지 코드 실행 우선순위
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

**Bean Validation 메시지 찾는 순서**
1. 생성된 메시지 코드 순서대로 `messageSource` 에서 메시지 찾기
2. 애노테이션의 message 속성 사용 : @NotBlank(message = "공백 X")
3. 라이브러리가 제공하는 기본 값 사용 : 공백일 수 없습니다.








# Spring Framework
[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard) 정리 문서

# 검증
### Validator  
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






















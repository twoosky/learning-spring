package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// [comment] @Component 어노테이션이 붙은 클래스를 찾아 스프링 빈으로 자동 등록해주는 어노테이션
// - excludeFilters 속성을 통해 스프링 빈 등록에서 제외할 클래스 지정 가능
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

}

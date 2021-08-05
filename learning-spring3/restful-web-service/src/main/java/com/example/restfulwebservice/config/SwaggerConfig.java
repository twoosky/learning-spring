package com.example.restfulwebservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// bean 등록
@Configuration       // 설정
@EnableSwagger2      // 스웨거 생성
public class SwaggerConfig {
    private static final Contact DEFAULT_CONTACT = new Contact("Haneul Lee",
            "http://www.joneconsulting.co.kr", "lhjae3@naver.com");

    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Awesome API Title",
            "My User management REST API service", "1.0", "urn:tos",
            DEFAULT_CONTACT, "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());

    // 문서 타입 지정
    // asList(): 배열을 리스트형태로 바꿔주는 함수
    private static final Set<String> DEFAULT_AND_CONSUMES = new HashSet<>(
            Arrays.asList("application/json","application/xml"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_AND_CONSUMES)
                .consumes(DEFAULT_AND_CONSUMES);
    }
}

package com.web.config;

import com.web.oauth.CustomOAuth2Provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.web.domain.enums.SocialType.FACEBOOK;
import static com.web.domain.enums.SocialType.GOOGLE;
import static com.web.domain.enums.SocialType.KAKAO;

/**
 * 이제 OAuth2 인증 프로세스를 적용하기 위해
 * addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class) 와 같은 필터를 추가하여
 * oauth2Filter()가 적용되도록 설정합니다.
 */
@Configuration   // @Configuration으로 등록되어 있는 클래스에서 @Bean으로 등록된 메소드의 파라미터로 지정된 객체들은 오토와이어링 할 수 있습니다.
@EnableWebSecurity    // 웹에서 시큐리티 기능을 사용하겠다는 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests()      // 인증 메커니즘을 요청한 HttpServletRequest 기반으로 설정합니다.
                    .antMatchers("/", "/oauth2/**", "/login/**",  "/css/**", "/images/**", "/js/**", "/console/**").permitAll()
                    // .antMatchers(): 요청 패턴을 리스트 형식으로 설정합니다.
                    // .permitAll(): 설정한 리쿼스트 패턴을 누구나 접근할 수 있도록 허용합니다.
                    .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
                    .antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
                    .antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())
                    .anyRequest().authenticated()
                    // .anyRequest(): 설정한 요청 이외의 리퀘스트 요청을 표현합니다. (즉 위 url 제외)
                    // .authenticated(): 해당 요청은 인증된 사용자만 할 수 있습니다.
                .and()
                    .oauth2Login()
                    .defaultSuccessUrl("/loginSuccess")
                    .failureUrl("/loginFailure")
                .and()
                    .headers().frameOptions().disable()
                    // .headers(): 응답에 해당하는 header를 설정합니다. (설정하지 않으면 디폴트값)
                    // .frameOptions().disable(): XFrameOptionsHeaderWriter의 최적화 설정을 허용하지 않습니다.
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                    // 인증의 진입 지점입니다.
                    //인증되지 않은 사용자가 허용되지 않은 경로로 리퀘스트를 요청할 경우 /login으로 이동됩니다.
                .and()
                    .formLogin()
                    .successForwardUrl("/board/list")
                    // 로그인에 성공하면 설정된 경로로 포워딩합니다.
                .and()
                    .logout()  // 로그아웃에 대한 설정을 할 수 있습니다.
                    .logoutUrl("/logout")  // 로그아웃이 수행될 url
                    .logoutSuccessUrl("/")  // 로그아웃이 성공했을 때 포워딩할 url
                    .deleteCookies("JSESSIONID")  // 로그아웃을 성공했을 때 삭제될 쿠키값
                    .invalidateHttpSession(true)  // 설정된 세션의 무효화 설정
                .and()
                    .addFilterBefore(filter, CsrfFilter.class)  // 첫 번째 인자보다 먼저 시작될 필터를 등록
                    .csrf().disable();
    }

    // OAuth2ClientProperties 는 @Configuration으로 등록되어 있는 클래스이므로 오토와이어링으로 값을 가져오고
    // 카카오는 따로 등록했기 때문에 @Value 어노테이션을 사용하여 수동으로 불러와줍니다.
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties, @Value("${custom.oauth2.kakao.client-id}") String kakaoClientId) {
        List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream()   // getRegistration() 메서드를 사용해 구글과 페이스북의 인증 정보를 빌드시켜줍니다.
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")     // registrations 리스트에 카카오 인증 정보를 추가해줍니다.
                .clientId(kakaoClientId)
                .clientSecret("test") //필요없는 값인데 null이면 실행이 안되도록 설정되어 있음
                .jwkSetUri("test") //필요없는 값인데 null이면 실행이 안되도록 설정되어 있음
                .build());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
        if ("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }
        if ("facebook".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build();
        }
        return null;
    }
}
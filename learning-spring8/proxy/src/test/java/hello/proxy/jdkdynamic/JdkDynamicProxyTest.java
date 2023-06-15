package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);  // 동적 프록시에 적용할 핸들러 로직

        /**
         * 자바 언어 차원에서 제공해주는 프록시 생성 기술
         * param = {클래스 로더 정보, 인터페이스, 핸들러 로직}
         * proxy 객체는 메서드가 호출되면 handler의 invoke 메서드 실행
         */
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);  // 동적 프록시에 적용할 핸들러 로직

        /**
         * 자바 언어 차원에서 제공해주는 프록시 생성 기술
         * param = {클래스 로더 정보, 인터페이스, 핸들러 로직}
         * proxy 객체는 메서드가 호출되면 handler의 invoke 메서드 실행
         */
        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);
        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}

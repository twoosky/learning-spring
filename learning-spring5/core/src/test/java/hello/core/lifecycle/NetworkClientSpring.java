package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClientSpring implements InitializingBean, DisposableBean {
    private String url;

    // [comment] 스프링 빈 라이프사이클
    // 생성자 호출 -> 의존관계 주입 -> afterPropertiesSet() 호출해 초기화 -> 로직 수행 -> destroy() 호출해 종료

    public NetworkClientSpring() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("Call " + url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close " + url);
    }

    // 의존 관계 주입이 끝나면 호출되는 메서드 오버라이딩
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    // 스프링 컨테이너 종료 전 호출되는 메서드 오버라이딩
    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}

package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {

    private Subject target;  // 실제 객체 (proxy가 호출할 대상)
    private String cacheValue;  // 캐시할 데이터 저장 변수

    /**
     * Proxy가 실제 객체 참조하기 위해 생성자 의존관계 주입
     * @param target
     */
    public CacheProxy(Subject target) {
        this.target = target;
    }

    /**
     * 캐시 처리
     */
    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}

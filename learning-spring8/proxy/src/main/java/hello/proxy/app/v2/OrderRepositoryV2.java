package hello.proxy.app.v2;

/**
 * v2 - 인터페이스 없는 구체 클래스 - 스프링 빈으로 수동 등록
 */
public class OrderRepositoryV2 {

    public void save(String itemId) {
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

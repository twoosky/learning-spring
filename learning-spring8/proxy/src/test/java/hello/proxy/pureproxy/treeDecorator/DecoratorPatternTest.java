package hello.proxy.pureproxy.treeDecorator;

import hello.proxy.pureproxy.treeDecorator.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {

    @Test
    void noDecorator() {
        Component realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        String result = client.execute();
        log.info("[noDecorator] result={}", result);
    }

    @Test
    void decorator() {
        Component realComponent = new RealComponent();
        StarDecorator starDecorator = new StarDecorator(realComponent);
        LightDecorator lightDecorator = new LightDecorator(starDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(lightDecorator);
        String result = client.execute();
        log.info("[decorator] result={}", result);
    }
}

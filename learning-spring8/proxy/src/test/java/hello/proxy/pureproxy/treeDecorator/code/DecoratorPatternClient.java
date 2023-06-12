package hello.proxy.pureproxy.treeDecorator.code;

public class DecoratorPatternClient {

    private Component component;

    public DecoratorPatternClient(Component component) {
        this.component = component;
    }

    public String execute() {
        return component.operation();
    }
}

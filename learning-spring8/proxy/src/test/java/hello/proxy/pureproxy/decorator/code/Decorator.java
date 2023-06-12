package hello.proxy.pureproxy.decorator.code;

abstract public class Decorator implements Component {

    private final Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        return component.operation();
    }
}

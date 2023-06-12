package hello.proxy.pureproxy.treeDecorator.code;

public class LightDecorator extends Decorator {

    public LightDecorator(Component component) {
        super(component);
    }

    @Override
    public String operation() {
        return super.operation() + "전구 ";
    }
}

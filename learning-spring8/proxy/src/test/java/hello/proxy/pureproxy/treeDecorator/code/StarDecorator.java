package hello.proxy.pureproxy.treeDecorator.code;

public class StarDecorator extends Decorator {

    public StarDecorator(Component component) {
        super(component);
    }

    @Override
    public String operation() {
        return super.operation() + "별 ";
    }
}

package hello.proxy.pureproxy.treeDecorator.code;

public class RealComponent implements Component {
    @Override
    public String operation() {
        return "크리스마스 트리 ";
    }
}

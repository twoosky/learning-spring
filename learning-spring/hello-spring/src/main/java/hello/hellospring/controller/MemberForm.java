package hello.hellospring.controller;

// form에 입력받은 데이터를 setName을 통해 name에 넣음.
public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

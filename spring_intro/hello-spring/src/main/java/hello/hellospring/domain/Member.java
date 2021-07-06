package hello.hellospring.domain;

import javax.persistence.*;

@Entity
public class Member {

    // IDENTITY : DB에서 id값을 자동으로 생성해줌.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

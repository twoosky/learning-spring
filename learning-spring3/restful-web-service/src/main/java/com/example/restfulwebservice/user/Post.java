package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    // User : Post -> 1 : (0~N), Main : Sub -> Parent : Child
    // Post 데이터는 여러개 올 수 있고, 하나의 User값과 매핑
    // fetch = FetchType.LAZY: 지연로딩방식, 사용자 Entity를 조회할 때 Post Entity가 같이 로딩되는 것이 아니라
    // Post data가 로딩되는 시점에 필요한 사용자 데이터를 가져옴
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore    // 외부에 데이터 노출되지 않음
    private User user;

}

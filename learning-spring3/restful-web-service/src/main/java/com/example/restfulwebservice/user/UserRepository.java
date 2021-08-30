package com.example.restfulwebservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository<> 상속 받을 시 해당 엔티티에 대한 CRUD를 공짜로 사용할 수 있게 된다.
public interface UserRepository extends JpaRepository<User, Integer> {
}

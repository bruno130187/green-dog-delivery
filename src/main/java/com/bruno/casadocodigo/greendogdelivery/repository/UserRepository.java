package com.bruno.casadocodigo.greendogdelivery.repository;


import com.bruno.casadocodigo.greendogdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userName =:userName")
    User findByUsername(@Param("userName") String userName);

    @Query("SELECT u.password FROM User u WHERE u.id =:id")
    String getPassAtual(@Param("id")Long id);
}



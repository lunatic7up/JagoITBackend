package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM USERS WHERE EMAIL=?1", nativeQuery = true)
    User findUser(String email);

    @Query(value = "SELECT * FROM USERS WHERE USER_ID=?1", nativeQuery = true)
    User getUserData(String user_id);
}

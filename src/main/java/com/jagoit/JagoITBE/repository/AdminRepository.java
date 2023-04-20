package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    @Query(value = "SELECT * FROM ADMIN WHERE EMAIL=?1", nativeQuery = true)
    Admin findAdmin(String email);

    @Query(value = "SELECT * FROM ADMIN WHERE USER_ID=?1", nativeQuery = true)
    Admin getAdminData(String user_id);

}

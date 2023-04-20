package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.helper.HelperConstant;
import com.jagoit.JagoITBE.model.Consultant;
import com.jagoit.JagoITBE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, String> {

    @Query(value = "SELECT * FROM CONSULTANT WHERE STATUS = 'pending'", nativeQuery = true)
    List<Consultant> getPendingConsultant();

    @Modifying
    @Query(value = "UPDATE CONSULTANT SET STATUS = " + HelperConstant.IS_REGISTERED + " WHERE EMAIL=?1", nativeQuery = true)
    void approveConsultant(String email);

    @Modifying
    @Query(value = "DELETE FROM CONSULTANT WHERE EMAIL=?1", nativeQuery = true)
    void rejectConsultant(String email);

    @Query(value = "SELECT * FROM CONSULTANT WHERE STATUS = 'active'", nativeQuery = true)
    List<Consultant> getAllConsultant();

    @Query(value = "SELECT * FROM CONSULTANT WHERE EMAIL=?1", nativeQuery = true)
    Consultant findConsultant(String email);

    @Query(value = "SELECT * FROM CONSULTANT WHERE EMAIL=?1 AND STATUS = 'active'", nativeQuery = true)
    Consultant findActiveConsultant(String email);

    @Query(value = "SELECT * FROM CONSULTANT WHERE USER_ID=?1", nativeQuery = true)
    Consultant getConsultantData(String user_id);
}

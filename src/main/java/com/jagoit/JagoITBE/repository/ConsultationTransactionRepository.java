package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.helper.HelperConstant;
import com.jagoit.JagoITBE.model.ConsultationTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface ConsultationTransactionRepository extends JpaRepository<ConsultationTransaction, String> {

    @Query(value = "SELECT * FROM TXN_CONSULTATION WHERE USER_ID_CONSULTANT=?1 AND (STATUS='requested' OR STATUS='onprogress') ORDER BY CONSULTATION_DATE ASC", nativeQuery = true)
    List<ConsultationTransaction> getUpcomingConsultation(String user_id);

    @Query(value = "SELECT * FROM TXN_CONSULTATION WHERE USER_ID_CONSULTEE=?1 AND (STATUS='requested' OR STATUS='onprogress') ORDER BY CONSULTATION_DATE DESC", nativeQuery = true)
    List<ConsultationTransaction> getListConsultation(String user_id);

    @Query(value = "SELECT TC.*, C.USER_NAME, C.PROFILE_PICTURE " +
            "FROM TXN_CONSULTATION TC JOIN CONSULTANT C ON TC.USER_ID_CONSULTANT=C.USER_ID " +
            "WHERE USER_ID_CONSULTEE=?1 AND (TC.STATUS='done' OR TC.STATUS='rejected') " +
            "ORDER BY CONSULTATION_DATE DESC", nativeQuery = true)
    List<Tuple> getHistoryConsultationByConsultee(String user_id);

    @Query(value = "SELECT TC.*, U.USER_NAME, U.PROFILE_PICTURE " +
            "FROM TXN_CONSULTATION TC JOIN USERS U ON TC.USER_ID_CONSULTEE=U.USER_ID " +
            "WHERE USER_ID_CONSULTANT=?1 AND (TC.STATUS='done' OR TC.STATUS='rejected') " +
            "ORDER BY CONSULTATION_DATE DESC", nativeQuery = true)
    List<Tuple> getHistoryConsultationByConsultant(String user_id);

    @Query(value = "SELECT * FROM TXN_CONSULTATION WHERE ID=?1", nativeQuery = true)
    ConsultationTransaction getDataConsultation(String id);

    @Query(value = "SELECT TC.*, U.USER_NAME AS CONSULTEE_NAME, U.EMAIL AS CONSULTEE_EMAIL, " +
            "U.PHONE_NUMBER AS CONSULTEE_PHONE_NUMBER, U.PROFILE_PICTURE AS CONSULTEE_PROFILE_PICTURE, " +
            "C.USER_NAME AS CONSULTANT_NAME, C.EMAIL AS CONSULTANT_EMAIL, " +
            "C.PHONE_NUMBER AS CONSULTANT_PHONE_NUMBER, C.PROFILE_PICTURE AS CONSULTANT_PROFILE_PICTURE " +
            "FROM TXN_CONSULTATION TC JOIN USERS U ON TC.USER_ID_CONSULTEE=U.USER_ID " +
            "JOIN CONSULTANT C ON TC.USER_ID_CONSULTANT=C.USER_ID " +
            "WHERE TC.ID=?1 AND TC.STATUS='" + HelperConstant.STATUS_DONE +"'", nativeQuery = true)
    Tuple getDataConsultationById(String id);

    @Query(value = "SELECT * FROM TXN_CONSULTATION WHERE CHAT_ID=?1", nativeQuery = true)
    ConsultationTransaction getDataConsultationByChatId(String chat_id);

    @Query(value = "SELECT TC.*, U.USER_NAME AS CONSULTEE_NAME, U.EMAIL AS CONSULTEE_EMAIL, " +
            "U.PHONE_NUMBER AS CONSULTEE_PHONE_NUMBER, U.PROFILE_PICTURE AS CONSULTEE_PROFILE_PICTURE, " +
            "C.USER_NAME AS CONSULTANT_NAME, C.EMAIL AS CONSULTANT_EMAIL, " +
            "C.PHONE_NUMBER AS CONSULTANT_PHONE_NUMBER, C.PROFILE_PICTURE AS CONSULTANT_PROFILE_PICTURE " +
            "FROM TXN_CONSULTATION TC JOIN USERS U ON TC.USER_ID_CONSULTEE=U.USER_ID " +
            "JOIN CONSULTANT C ON TC.USER_ID_CONSULTANT=C.USER_ID " +
            "WHERE CONSULTATION_DATE >= TO_DATE(?1, 'yyyy-MM-dd') AND " +
            "CONSULTATION_DATE <= TO_DATE(?2, 'yyyy-MM-dd') + 1", nativeQuery = true)
    List<Tuple> getConsultationByDate(String start_date, String end_date);

}

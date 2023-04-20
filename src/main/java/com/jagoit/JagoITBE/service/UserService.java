package com.jagoit.JagoITBE.service;

import com.jagoit.JagoITBE.model.Admin;
import com.jagoit.JagoITBE.model.Consultant;
import com.jagoit.JagoITBE.model.User;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

public interface UserService {


    User registerUser(Map<String, Object> paramMap);

    Consultant registerConsultant(Map<String, Object> paramMap);

    User getUser(String email);

    Consultant updateConsultantBalance(Integer nominal, String user_id);

    Consultant getConsultant(String email);

    Admin getAdmin(String email);

    Consultant getActiveConsultant(String email);

    User updateUserBalance(Integer nominal, String user_id);

    User editProfileUser(Map<String, Object> paramMap);

    List<Consultant> getAllConsultant();

    List<Consultant> getPendingConsultant();

    Runnable sendEmail(Map<String, Object> paramMap);

    Runnable sendEmailRegister(Map<String, Object> paramMap);

    void approveConsultant(String email);

    void rejectConsultant(String email);

    User getUserData(String user_id);

    Consultant getConsultantData(String user_id);

    Admin getAdminData(String user_id);

    User updateSubscription(String user_id, String status);

    Consultant editProfileConsultant(Map<String, Object> paramMap);

    Consultant updateRating(String id, Double rating);

}

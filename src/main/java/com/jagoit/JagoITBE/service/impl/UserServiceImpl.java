package com.jagoit.JagoITBE.service.impl;

import com.jagoit.JagoITBE.helper.HelperConstant;
import com.jagoit.JagoITBE.model.Admin;
import com.jagoit.JagoITBE.model.Consultant;
import com.jagoit.JagoITBE.model.User;
import com.jagoit.JagoITBE.repository.AdminRepository;
import com.jagoit.JagoITBE.repository.ConsultantRepository;
import com.jagoit.JagoITBE.repository.UserRepository;
import com.jagoit.JagoITBE.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public User registerUser(Map<String, Object> paramMap) {
        logger.debug(">> registerUser");
        User user = new User();
        String id = "US";
        id = id + new UUIDHexGenerator().generate(null, null).toString();
        user.setUser_id(id);
        user.setUser_name(paramMap.get("user_name").toString());
        user.setUser_password(paramMap.get("user_password").toString());
        user.setEmail(paramMap.get("email").toString());
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(paramMap.get("birth_date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setBirth_date(date);
        user.setGender(paramMap.get("gender").toString());
        if(paramMap.containsKey("profile_picture") && !paramMap.get("profile_picture").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("profile_picture").toString());
            user.setProfile_picture(imageBytes);
        }else{
            user.setProfile_picture(null);
        }
        user.setBalance(0);
        user.setPhone_number(paramMap.get("phone_number").toString());
        user.setCreated_date(new Timestamp(System.currentTimeMillis()));
        user.setIs_subscription("N");
        user.setStart_date_subscription(null);
        user.setEnd_date_subscription(null);
        return userRepository.save(user);
    }

    @Override
    public Consultant registerConsultant(Map<String, Object> paramMap) {
        logger.debug(">> registerConsultant");
        Consultant consultant = new Consultant();
        String id = "US";
        id = id + new UUIDHexGenerator().generate(null, null).toString();
        consultant.setUser_id(id);
        consultant.setUser_name(paramMap.get("user_name").toString());
        consultant.setUser_password(paramMap.get("user_password").toString());
        consultant.setEmail(paramMap.get("email").toString());
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(paramMap.get("birth_date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        consultant.setBirth_date(date);
        consultant.setGender(paramMap.get("gender").toString());
        if(paramMap.containsKey("profile_picture") && !paramMap.get("profile_picture").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("profile_picture").toString());
            consultant.setProfile_picture(imageBytes);
        }else{
            consultant.setProfile_picture(null);
        }
        if(paramMap.containsKey("doc_picture_1") && !paramMap.get("doc_picture_1").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_1").toString());
            consultant.setDoc_picture_1(imageBytes);
        }else{
            consultant.setDoc_picture_1(null);
        }
        if(paramMap.containsKey("doc_picture_2") && !paramMap.get("doc_picture_2").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_2").toString());
            consultant.setDoc_picture_2(imageBytes);
        }else{
            consultant.setDoc_picture_2(null);
        }
        if(paramMap.containsKey("doc_picture_3") && !paramMap.get("doc_picture_3").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_3").toString());
            consultant.setDoc_picture_3(imageBytes);
        }else{
            consultant.setDoc_picture_3(null);
        }
        if(paramMap.containsKey("doc_picture_4") && !paramMap.get("doc_picture_4").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_4").toString());
            consultant.setDoc_picture_4(imageBytes);
        }else{
            consultant.setDoc_picture_4(null);
        }
        if(paramMap.containsKey("doc_picture_5") && !paramMap.get("doc_picture_5").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_5").toString());
            consultant.setDoc_picture_5(imageBytes);
        }else{
            consultant.setDoc_picture_5(null);
        }
        consultant.setBalance(0);
        consultant.setPhone_number(paramMap.get("phone_number").toString());
        consultant.setCreated_date(new Timestamp(System.currentTimeMillis()));
        consultant.setPrice(Integer.parseInt(paramMap.get("price").toString()));
        consultant.setRating(0.0);
        consultant.setCount_rating(0);
        consultant.setWorktime(paramMap.get("worktime").toString());
        consultant.setLast_education(paramMap.get("last_education").toString());
        consultant.setAddress(paramMap.get("address").toString());
        consultant.setCategory(paramMap.get("category").toString());
        consultant.setStatus(HelperConstant.NON_REGISTERED);
        consultant.setOcupation(paramMap.get("ocupation").toString());
        return consultantRepository.save(consultant);
    }

    @Override
    public User getUser(String email) {
        logger.debug(">> getUser");
        return userRepository.findUser(email);
    }

    @Override
    public Consultant getConsultant(String email) {
        logger.debug(">> getConsultant");
        return consultantRepository.findConsultant(email);
    }

    @Override
    public Consultant getActiveConsultant(String email) {
        logger.debug(">> getActiveConsultant");
        return consultantRepository.findActiveConsultant(email);
    }

    @Override
    public Admin getAdmin(String email) {
        logger.debug(">> getAdmin");
        return adminRepository.findAdmin(email);
    }

    public List<Consultant> getPendingConsultant(){
        logger.debug(">> getPendingConsultant");
        return consultantRepository.getPendingConsultant();
    }

    @Override
    public void approveConsultant(String email) {
        logger.debug(">> approveConsultant");
        consultantRepository.approveConsultant(email);
    }

    @Override
    public void rejectConsultant(String email) {
        logger.debug(">> rejectConsultant");
        consultantRepository.rejectConsultant(email);
    }

    @Override
    public User getUserData(String user_id) {
        return userRepository.getUserData(user_id);
    }

    @Override
    public Consultant getConsultantData(String user_id) {
        return consultantRepository.getConsultantData(user_id);
    }

    @Override
    public Admin getAdminData(String user_id) {
        return adminRepository.getAdminData(user_id);
    }

    @Override
    public User updateSubscription(String user_id, String status) {
        logger.debug(">> updateSubscription");
        User user = userRepository.getById(user_id);
        user.setIs_subscription(status);
        if(status.equals("Y")){
            user.setStart_date_subscription(new Timestamp(System.currentTimeMillis()));
            long endTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(365, TimeUnit.DAYS);
            user.setEnd_date_subscription(new Timestamp(endTime));
        }
        return userRepository.save(user);
    }

    @Override
    public Consultant editProfileConsultant(Map<String, Object> paramMap) {
        logger.debug(">> editProfileConsultant");
        Consultant consultant = consultantRepository.getById(paramMap.get("user_id").toString());
        consultant.setUser_name(paramMap.get("user_name").toString());
        consultant.setBank_name(paramMap.get("bank_name").toString());
        consultant.setAccount_number(paramMap.get("account_number").toString());
        consultant.setPhone_number(paramMap.get("phone_number").toString());
        consultant.setLast_education(paramMap.get("last_education").toString());
        consultant.setOcupation(paramMap.get("ocupation").toString());
        consultant.setWorktime(paramMap.get("worktime").toString());
        consultant.setAddress(paramMap.get("address").toString());
        consultant.setPrice(Integer.parseInt(paramMap.get("price").toString()));
        consultant.setCategory(paramMap.get("category").toString());
        consultant.setNote(paramMap.get("note").toString());
        if(paramMap.containsKey("profile_picture") && !paramMap.get("profile_picture").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("profile_picture").toString());
            consultant.setProfile_picture(imageBytes);
        }
        if(paramMap.containsKey("doc_picture_1") && !paramMap.get("doc_picture_1").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_1").toString());
            consultant.setDoc_picture_1(imageBytes);
        }
        if(paramMap.containsKey("doc_picture_2") && !paramMap.get("doc_picture_2").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_2").toString());
            consultant.setDoc_picture_2(imageBytes);
        }
        if(paramMap.containsKey("doc_picture_3") && !paramMap.get("doc_picture_3").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_3").toString());
            consultant.setDoc_picture_3(imageBytes);
        }
        if(paramMap.containsKey("doc_picture_4") && !paramMap.get("doc_picture_4").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_4").toString());
            consultant.setDoc_picture_4(imageBytes);
        }
        if(paramMap.containsKey("doc_picture_5") && !paramMap.get("doc_picture_5").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("doc_picture_5").toString());
            consultant.setDoc_picture_5(imageBytes);
        }
        return consultantRepository.save(consultant);
    }

    @Override
    public Consultant updateRating(String id, Double rating) {
        logger.debug(">> updateRating");
        Consultant consultant = consultantRepository.getById(id);
        Integer countRating = consultant.getCount_rating();
        Double userRating = consultant.getRating();
        consultant.setCount_rating(countRating + 1);
        consultant.setUpdated_date(new Timestamp(System.currentTimeMillis()));
        Double ratingAvg = ((userRating * countRating ) + rating) / (countRating + 1);

        Double ratingAvgPrecision = BigDecimal.valueOf(ratingAvg)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        consultant.setRating(ratingAvgPrecision);
        return consultantRepository.save(consultant);
    }

    @Override
    public User updateUserBalance(Integer nominal, String user_id) {
        logger.debug(">> updateUserBalance");
        User user = userRepository.getById(user_id);
        Integer userBalance = user.getBalance();
        userBalance = userBalance + nominal;
        user.setBalance(userBalance);
        user.setUpdated_date(new Timestamp(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Override
    public Consultant updateConsultantBalance(Integer nominal, String user_id) {
        logger.debug(">> updateUserBalance");
        Consultant consultant = consultantRepository.getById(user_id);
        Integer userBalance = consultant.getBalance();
        userBalance = userBalance + nominal;
        consultant.setBalance(userBalance);
        consultant.setUpdated_date(new Timestamp(System.currentTimeMillis()));
        return consultantRepository.save(consultant);
    }

    @Override
    public User editProfileUser(Map<String, Object> paramMap) {
        logger.debug(">> editProfileUser");
        User user = userRepository.getById(paramMap.get("user_id").toString());

        if(!paramMap.get("profile_picture").equals("")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("profile_picture").toString());
            user.setProfile_picture(imageBytes);
        }
        user.setBank_name(paramMap.get("bank_name").toString());
        user.setAccount_number(paramMap.get("account_number").toString());
        user.setPhone_number(paramMap.get("phone_number").toString());
        user.setUser_name(paramMap.get("user_name").toString());
        user.setUpdated_date(new Timestamp(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Override
    public List<Consultant> getAllConsultant() {
        logger.debug(">> getAllConsultant");
        return consultantRepository.getAllConsultant();
    }

    @Override
    public Runnable sendEmail(Map<String, Object> paramMap) {
        logger.debug("sendEmail");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jagoitppti8@gmail.com");

        message.setTo(paramMap.get("email").toString());
        message.setSubject("Pendaftaran Akun Expert di Jago IT");
        if(paramMap.get("activity").equals(HelperConstant.APPROVE_ACTIVITY)){
            message.setText("Pendaftaran akun anda di Jago IT diterima. Selamat bergabung di Jago IT.\n\nRegards,\nAdmin Jago IT");
        }else{
            message.setText("Pendaftaran akun anda di Jago IT ditolak dikarenakan " + paramMap.get("notes").toString() + ".\n\nRegards,\nAdmin Jago IT");
        }
        return new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++){
                    try{
                        javaMailSender.send(message);
                        break;
                    }catch (Exception e){
                        logger.debug(e);
                        i++;
                    }

                }

            }
        };
    }

    @Override
    public Runnable sendEmailRegister(Map<String, Object> paramMap) {
        logger.debug("sendEmail");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jagoitppti8@gmail.com");

        message.setTo(paramMap.get("email").toString());
        message.setSubject("Pendaftaran Akun di Jago IT");
        message.setText("Pendaftaran akun anda di Jago IT berhasil. Selamat bergabung di Jago IT.\n\nRegards,\nAdmin Jago IT");

        return new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++){
                    try{
                        javaMailSender.send(message);
                        break;
                    }catch (Exception e){
                        logger.debug(e);
                        i++;
                    }

                }

            }
        };
    }

}

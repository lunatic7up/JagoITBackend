package com.jagoit.JagoITBE.service.impl;

import com.jagoit.JagoITBE.helper.HelperConstant;
import com.jagoit.JagoITBE.helper.Utils;
import com.jagoit.JagoITBE.model.ConsultationTransaction;
import com.jagoit.JagoITBE.model.User;
import com.jagoit.JagoITBE.repository.ConsultationTransactionRepository;
import com.jagoit.JagoITBE.repository.UserRepository;
import com.jagoit.JagoITBE.service.ConsultationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.id.UUIDHexGenerator;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private static final Logger logger = LogManager.getLogger(ConsultationServiceImpl.class);

    @Autowired
    private ConsultationTransactionRepository consultationTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public ConsultationTransaction createConsultation(Map<String, Object> paramMap) throws ParseException {
        logger.debug(">> createConsultation");
        ConsultationTransaction consultationTransaction = new ConsultationTransaction();

        String id = "JICS-";
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String dates = dateFormat.format(date);
        id = id + dates + "-" + new UUIDHexGenerator().generate(null, null).toString();

        consultationTransaction.setConsultation_id(id);
        consultationTransaction.setUser_id_consultee(paramMap.get("userIdConsultee").toString());
        consultationTransaction.setUser_id_consultant(paramMap.get("userIdConsultant").toString());
        consultationTransaction.setConsultation_type(paramMap.get("consultationType").toString());
        consultationTransaction.setAdmin_fee(Integer.parseInt(paramMap.get("adminFee").toString()));
        consultationTransaction.setConsultation_fee(Integer.parseInt(paramMap.get("consultationFee").toString()));
        consultationTransaction.setConsultation_method(paramMap.get("consultationMethod").toString());

        String stringDate = paramMap.get("consultationDate").toString();
        Timestamp timestamp = new java.sql.Timestamp(new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(stringDate).getTime());
        logger.debug("date: " + timestamp);
        consultationTransaction.setConsultation_date(timestamp);

        consultationTransaction.setStatus(HelperConstant.STATUS_REQUESTED);
        consultationTransaction.setCreated_date(new Timestamp(System.currentTimeMillis()));
        return consultationTransactionRepository.save(consultationTransaction);
    }

    @Override
    public List<ConsultationTransaction> getUpcomingConsultation(String user_id) {
//        logger.debug(">> getUpcomingConsultation");
        return consultationTransactionRepository.getUpcomingConsultation(user_id);
    }

    @Override
    public List<ConsultationTransaction> getListConsultation(String user_id) {
        logger.debug(">> getUpcomingConsultation");
        return consultationTransactionRepository.getListConsultation(user_id);
    }

    @Override
    public ConsultationTransaction updateStatusConsultation(String id, String activity) {
        logger.debug(">> updateStatusConsultation");
        ConsultationTransaction consultationTransaction = consultationTransactionRepository.getById(id);
        if(consultationTransaction.getStatus().equals("cancelled")){
            return null;
        }
        if(activity.equals(HelperConstant.APPROVE_ACTIVITY)){
            String type = consultationTransaction.getConsultation_type();
            if(type.equals("now")){
                consultationTransaction.setStatus(HelperConstant.STATUS_ONPROGRESS);
            }else{
                consultationTransaction.setStatus(HelperConstant.STATUS_WAITING);
            }
        }else{
            consultationTransaction.setStatus(HelperConstant.STATUS_REJECTED);
        }
        return consultationTransactionRepository.save(consultationTransaction);
    }


    public Runnable updateDoneOnProrgress(String id){

        return new Runnable() {
            @Override
            public void run() {
                long times = TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS);
                logger.debug("times: " + times);
                int i = 0;
                try {
                    Thread.sleep(times);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.debug("masuk");
                ConsultationTransaction consultationTransaction = consultationTransactionRepository.getDataConsultation(id);
                consultationTransaction.setStatus(HelperConstant.STATUS_DONE);
                consultationTransactionRepository.save(consultationTransaction);
            }
        };

    }


    @Override
    public Runnable checkStatusConsultation(String id, String type) {
        MDC.put("processID", Utils.generate_process_id());
        return new Runnable() {
            @Override
            public void run() {
                long endtime = 0;
                if(type.equals("now")){
                    endtime = TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS);
                    try{
                        Thread.sleep(endtime);
                        ConsultationTransaction consultationTransaction = consultationTransactionRepository.getDataConsultation(id);
                        String user_id_consultee = consultationTransaction.getUser_id_consultee();
                        logger.debug("user_id: " + user_id_consultee);
                        if(consultationTransaction.getStatus().equals(HelperConstant.STATUS_REQUESTED)){
                            consultationTransaction.setStatus(HelperConstant.STATUS_CANCELLED);
                            consultationTransactionRepository.save(consultationTransaction);

                            User user = userRepository.getUserData(user_id_consultee);
                            Integer balance = user.getBalance();
                            balance += consultationTransaction.getConsultation_fee() + consultationTransaction.getAdmin_fee();
                            user.setBalance(balance);
                            user.setUpdated_date(new Timestamp(System.currentTimeMillis()));
                            userRepository.save(user);
                            SimpleMailMessage message = new SimpleMailMessage();
                            message.setFrom("jagoitppti8@gmail.com");
                            message.setTo(user.getEmail());
                            message.setSubject("Konsultasi di Jago IT");
                            message.setText("Konsultasi bersama XXXX dengan nomor transaksi " + consultationTransaction.getConsultation_id() + " telah dibatalkan. " +
                                    "Biaya konsultasi akan dikembalikan ke saldo anda.\n\nRegards,\nJago IT");
                            javaMailSender.send(message);
                        }
                    }catch(Exception e){
                        logger.debug(e);
                    }
                }else{
                    ConsultationTransaction consultationTransaction = consultationTransactionRepository.getDataConsultation(id);
                    Timestamp date = consultationTransaction.getConsultation_date();
                    endtime = date.getTime() - System.currentTimeMillis();
                    logger.debug("endTime: " + endtime);
                    try{
                        Thread.sleep(endtime);
                        consultationTransaction = consultationTransactionRepository.getDataConsultation(id);
                        String user_id_consultee = consultationTransaction.getUser_id_consultee();
                        if(consultationTransaction.getStatus().equals(HelperConstant.STATUS_REQUESTED)){
                            consultationTransaction.setStatus(HelperConstant.STATUS_CANCELLED);
                            consultationTransactionRepository.save(consultationTransaction);

                            User user = userRepository.getUserData(user_id_consultee);
                            Integer balance = user.getBalance();
                            balance += consultationTransaction.getConsultation_fee() + consultationTransaction.getAdmin_fee();
                            user.setBalance(balance);
                            user.setUpdated_date(new Timestamp(System.currentTimeMillis()));
                            userRepository.save(user);
                            SimpleMailMessage message = new SimpleMailMessage();
                            message.setFrom("jagoitppti8@gmail.com");
                            message.setTo(user.getEmail());
                            message.setSubject("Konsultasi di Jago IT");
                            message.setText("Konsultasi bersama XXXX dengan nomor transaksi " + consultationTransaction.getConsultation_id() + " telah dibatalkan. " +
                                    "Biaya konsultasi akan dikembalikan ke saldo anda.\n\nRegards,\nJago IT");
                            javaMailSender.send(message);
                        }else if(consultationTransaction.getStatus().equals(HelperConstant.STATUS_WAITING)){
                            consultationTransaction.setStatus(HelperConstant.STATUS_ONPROGRESS);
                            consultationTransactionRepository.save(consultationTransaction);
                            ExecutorService executor = Executors.newFixedThreadPool(4);
                            executor.execute(updateDoneOnProrgress(id));
                        }
                    }catch (Exception e){
                        logger.debug(e);
                    }
                }

            }
        };
    }

    @Override
    public ConsultationTransaction updateConsultation(String consultation_id, String chat_id) {
        ConsultationTransaction consultationTransaction = consultationTransactionRepository.getById(consultation_id);
        consultationTransaction.setChat_id(chat_id);
        return consultationTransactionRepository.save(consultationTransaction);
    }

    @Override
    public Runnable sendEmail(Map<String, Object> paramMap) {
        logger.debug("sendEmail");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jagoitppti8@gmail.com");
        message.setTo(paramMap.get("email_consultee").toString());
        message.setSubject("Konsultasi di Jago IT");
        if(paramMap.get("activity").equals("approve")){
            message.setText("Konsultasi di jago it dengan " + paramMap.get("consultant_name").toString() +
                    " pada tanggal " + paramMap.get("consultation_date") +
                    " dengan id: " + paramMap.get("consultation_id").toString() +
                    ".\n\nRegards,\nJago IT");
        }else{
            message.setText("Konsultasi di jago it dengan " + paramMap.get("consultant_name").toString() +
                    " telah ditolak dan dibatalkan. Uang akan dikembalikan ke saldo anda.\n\nRegards,\nJago IT");
        }

        SimpleMailMessage message1 = new SimpleMailMessage();
        message1.setFrom("jagoitppti8@gmail.com");
        message1.setTo(paramMap.get("email_consultant").toString());
        message1.setSubject("Konsultasi di Jago IT");
        if(paramMap.get("activity").equals("approve")){
            message1.setText("Konsultasi di jago it dengan " + paramMap.get("consultee_name").toString() +
                    " pada tanggal " + paramMap.get("consultation_date") +
                    " dengan id: " + paramMap.get("consultation_id").toString() +
                    ".\n\nRegards,\nJago IT");
        }else{
            message1.setText("Konsultasi di jago it dengan " + paramMap.get("consultee_name").toString() +
                    " telah dibatalkan. \n\nRegards,\nJago IT");
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

                for(int i = 0; i < 3; i++){
                    try{
                        javaMailSender.send(message1);
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
    public List<Tuple> getHistoryConsultation(String user_id, String role) {
        logger.debug("getHistoryConsultation");
        if(role.equalsIgnoreCase("consultee")){
            return consultationTransactionRepository.getHistoryConsultationByConsultee(user_id);
        }else{
            return consultationTransactionRepository.getHistoryConsultationByConsultant(user_id);
        }
    }

    @Override
    public ConsultationTransaction updateRating(String id, Double rating) {
        logger.debug("getHistoryConsultation");
        ConsultationTransaction consultationTransaction = consultationTransactionRepository.getById(id);
        consultationTransaction.setRating(rating);
        return consultationTransactionRepository.save(consultationTransaction);
    }

    @Override
    public ConsultationTransaction reportConsultation(Map<String, Object> paramMap) {
        logger.debug("reportConsultation");
        String id = paramMap.get("id").toString();
        ConsultationTransaction consultationTransaction = consultationTransactionRepository.getById(id);
        if(paramMap.get("role").toString().equals("consultee")){
            consultationTransaction.setIs_reported_consultant("Y");
        }else{
            consultationTransaction.setIs_reported_consultee("Y");
        }
        consultationTransaction.setUpdated_date(new Timestamp(System.currentTimeMillis()));
        return consultationTransactionRepository.save(consultationTransaction);
    }

    @Override
    public Runnable sendEmailReportConsultation(Map<String, Object> paramMap) {
        logger.debug("sendEmailReportConsultation");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jagoitppti8@gmail.com");
        message.setTo("jagoitppti8@gmail.com");
        message.setSubject("Report konsultasi dengan id " + paramMap.get("id").toString());
        if(paramMap.get("role").equals("consultee")){
            message.setText(paramMap.get("userNameConsultee").toString() + " telah melaporkan konsultasi bersama " +
                    paramMap.get("userNameConsultant").toString() + " dengan alasan " + paramMap.get("notes").toString());
        }else{
            message.setText(paramMap.get("userNameConsultant").toString() + " telah melaporkan konsultasi bersama " +
                    paramMap.get("userNameConsultee").toString() + " dengan alasan " + paramMap.get("notes").toString());
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
    public Tuple getConsultationById(String consultation_id) {
        logger.debug("getConsultationById");
        return consultationTransactionRepository.getDataConsultationById(consultation_id);
    }

    @Override
    public ConsultationTransaction getConsultationByChatId(String chat_id) {
        logger.debug("getConsultationByChatId");
        return consultationTransactionRepository.getDataConsultationByChatId(chat_id);
    }

    @Override
    public ConsultationTransaction finishConsultation(String consultation_id) {
        logger.debug("finishConsultation");
        ConsultationTransaction consultationTransaction = consultationTransactionRepository.getById(consultation_id);
        consultationTransaction.setStatus(HelperConstant.STATUS_DONE);
        return consultationTransactionRepository.save(consultationTransaction);
    }

    @Override
    public List<Tuple> getConsultationByDate(String start_date, String end_date) {
        logger.debug("getConsultationByDate");
        return consultationTransactionRepository.getConsultationByDate(start_date, end_date);
    }


}

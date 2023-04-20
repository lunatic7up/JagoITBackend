package com.jagoit.JagoITBE.service;

import com.jagoit.JagoITBE.model.ConsultationTransaction;

import javax.persistence.Tuple;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ConsultationService {

    ConsultationTransaction createConsultation(Map<String, Object> paramMap) throws ParseException;

    List<ConsultationTransaction> getUpcomingConsultation(String user_id);

    List<ConsultationTransaction> getListConsultation(String user_id);

    ConsultationTransaction updateStatusConsultation(String id, String activity);

    Runnable checkStatusConsultation(String id, String type);

    ConsultationTransaction updateConsultation(String consultation_id, String chat_id);

    Runnable sendEmail(Map<String, Object> paramMap);

    Runnable updateDoneOnProrgress(String id);

    List<Tuple> getHistoryConsultation(String user_id, String role);

    ConsultationTransaction updateRating(String id, Double rating);

    ConsultationTransaction reportConsultation(Map<String, Object> paramMap);

    Runnable sendEmailReportConsultation(Map<String, Object> paramMap);

    Tuple getConsultationById(String consultation_id);

    ConsultationTransaction getConsultationByChatId(String chat_id);

    ConsultationTransaction finishConsultation(String consultation_id);

    List<Tuple> getConsultationByDate(String start_date, String end_date);
}

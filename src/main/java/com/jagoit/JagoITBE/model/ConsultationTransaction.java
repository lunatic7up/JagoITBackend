package com.jagoit.JagoITBE.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "txn_consultation")
public class ConsultationTransaction {

    @Id
    @Column(name = "id")
    private String consultation_id;

    @Column(name = "user_id_consultee")
    private String user_id_consultee;

    @Column(name = "user_id_consultant")
    private String user_id_consultant;

    @Column(name = "consultation_type")
    private String consultation_type;

    @Column(name = "consultation_method")
    private String consultation_method;

    @Column(name = "created_date")
    private Timestamp created_date;

    @Column(name = "consultation_date")
    private Timestamp consultation_date;

    @Column(name = "updated_date")
    private Timestamp updated_date;

    @Column(name = "status")
    private String status;

    @Column(name = "consultation_fee")
    private Integer consultation_fee;

    @Column(name = "admin_fee")
    private Integer admin_fee;

    @Column(name = "chat_id")
    private String chat_id;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "is_reported_consultant")
    private String is_reported_consultant;

    @Column(name = "is_reported_consultee")
    private String is_reported_consultee;


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getIs_reported_consultant() {
        return is_reported_consultant;
    }

    public void setIs_reported_consultant(String is_reported_consultant) {
        this.is_reported_consultant = is_reported_consultant;
    }

    public String getIs_reported_consultee() {
        return is_reported_consultee;
    }

    public void setIs_reported_consultee(String is_reported_consultee) {
        this.is_reported_consultee = is_reported_consultee;
    }

    public Timestamp getConsultation_date() {
        return consultation_date;
    }

    public void setConsultation_date(Timestamp consultation_date) {
        this.consultation_date = consultation_date;
    }

    public String getConsultation_method() {
        return consultation_method;
    }

    public void setConsultation_method(String consultation_method) {
        this.consultation_method = consultation_method;
    }



    public String getConsultation_id() {
        return consultation_id;
    }

    public void setConsultation_id(String consultation_id) {
        this.consultation_id = consultation_id;
    }

    public String getUser_id_consultee() {
        return user_id_consultee;
    }

    public void setUser_id_consultee(String user_id_consultee) {
        this.user_id_consultee = user_id_consultee;
    }

    public String getUser_id_consultant() {
        return user_id_consultant;
    }

    public void setUser_id_consultant(String user_id_consultant) {
        this.user_id_consultant = user_id_consultant;
    }

    public String getConsultation_type() {
        return consultation_type;
    }

    public void setConsultation_type(String consultation_type) {
        this.consultation_type = consultation_type;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getConsultation_fee() {
        return consultation_fee;
    }

    public void setConsultation_fee(Integer consultation_fee) {
        this.consultation_fee = consultation_fee;
    }

    public Integer getAdmin_fee() {
        return admin_fee;
    }

    public void setAdmin_fee(Integer admin_fee) {
        this.admin_fee = admin_fee;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }
}

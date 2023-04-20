package com.jagoit.JagoITBE.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "user_id")
    private String user_id;

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_date")
    private Date birth_date;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "profile_picture")
    private byte[] profile_picture;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "created_date")
    private Timestamp created_date;

    @Column(name = "UPDATED_DATE")
    private Timestamp updated_date;

    @Column(name = "BANK_NAME")
    private String bank_name;

    @Column(name = "ACCOUNT_NUMBER")
    private String account_number;

    @Column(name = "is_subscription")
    private String is_subscription;

    @Column(name = "start_date_subscription")
    private Date start_date_subscription;

    @Column(name = "end_date_subscription")
    private Date end_date_subscription;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public byte[] getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(byte[] profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Date getCreated_date() {
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

    public String getIs_subscription() {
        return is_subscription;
    }

    public void setIs_subscription(String is_subscription) {
        this.is_subscription = is_subscription;
    }

    public Date getStart_date_subscription() {
        return start_date_subscription;
    }

    public void setStart_date_subscription(Date start_date_subscription) {
        this.start_date_subscription = start_date_subscription;
    }

    public Date getEnd_date_subscription() {
        return end_date_subscription;
    }

    public void setEnd_date_subscription(Date end_date_subscription) {
        this.end_date_subscription = end_date_subscription;
    }
}

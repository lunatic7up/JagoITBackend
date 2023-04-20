package com.jagoit.JagoITBE.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "CONSULTANT")
public class Consultant {

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

    @Column(name = "STATUS")
    private String status;

    @Column(name = "price")
    private Integer price;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "last_education")
    private String last_education;

    @Column(name = "address")
    private String address;

    @Column(name = "worktime")
    private String worktime;

    @Column(name = "ocupation")
    private String ocupation;

    @Column(name = "category")
    private String category;

    @Column(name = "DOC_PICTURE_1")
    private byte[] doc_picture_1;

    @Column(name = "DOC_PICTURE_2")
    private byte[] doc_picture_2;

    @Column(name = "DOC_PICTURE_3")
    private byte[] doc_picture_3;

    @Column(name = "DOC_PICTURE_4")
    private byte[] doc_picture_4;

    @Column(name = "DOC_PICTURE_5")
    private byte[] doc_picture_5;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "COUNT_RATING")
    private Integer count_rating;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getLast_education() {
        return last_education;
    }

    public void setLast_education(String last_education) {
        this.last_education = last_education;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }

    public String getOcupation() {
        return ocupation;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getDoc_picture_1() {
        return doc_picture_1;
    }

    public void setDoc_picture_1(byte[] doc_picture_1) {
        this.doc_picture_1 = doc_picture_1;
    }

    public byte[] getDoc_picture_2() {
        return doc_picture_2;
    }

    public void setDoc_picture_2(byte[] doc_picture_2) {
        this.doc_picture_2 = doc_picture_2;
    }

    public byte[] getDoc_picture_3() {
        return doc_picture_3;
    }

    public void setDoc_picture_3(byte[] doc_picture_3) {
        this.doc_picture_3 = doc_picture_3;
    }

    public byte[] getDoc_picture_4() {
        return doc_picture_4;
    }

    public void setDoc_picture_4(byte[] doc_picture_4) {
        this.doc_picture_4 = doc_picture_4;
    }

    public byte[] getDoc_picture_5() {
        return doc_picture_5;
    }

    public void setDoc_picture_5(byte[] doc_picture_5) {
        this.doc_picture_5 = doc_picture_5;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCount_rating() {
        return count_rating;
    }

    public void setCount_rating(Integer count_rating) {
        this.count_rating = count_rating;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

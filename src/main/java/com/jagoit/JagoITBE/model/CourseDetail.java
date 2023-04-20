package com.jagoit.JagoITBE.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "COURSE_DETAIL")
public class CourseDetail {

    @Id
    @Column(name = "COURSE_DETAIL_ID")
    private String course_detail_id;

    @Column(name = "COURSE_ID")
    private String course_id;

    @Column(name = "COURSE_DETAIL_NAME")
    private String course_detail_name;

    @Column(name = "COURSE_DETAIL_TYPE")
    private String course_detail_type;

    @Column(name = "COURSE_DETAIL_CONTENT_BYTE")
    private byte[] course_detail_content_byte;

    @Column(name = "COURSE_DETAIL_CONTENT_TEXT")
    private String course_detail_content_text;

    @Column(name = "COURSE_DETAIL_CONTENT_ASSESSMENT")
    private String course_detail_content_assessment;

    @Column(name = "COURSE_DETAIL_DESC")
    private String course_detail_desc;

    @Column(name = "IS_SUBSCRIPTION")
    private String is_subscription;

    @Column(name = "CREATED_DATE")
    private Timestamp created_date;

    @Column(name = "UPDATED_DATE")
    private Timestamp updated_date;

    public String getCourse_detail_desc() {
        return course_detail_desc;
    }

    public void setCourse_detail_desc(String course_detail_desc) {
        this.course_detail_desc = course_detail_desc;
    }

    public Timestamp getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Timestamp updated_date) {
        this.updated_date = updated_date;
    }

    public byte[] getCourse_detail_content_byte() {
        return course_detail_content_byte;
    }

    public void setCourse_detail_content_byte(byte[] course_detail_content_byte) {
        this.course_detail_content_byte = course_detail_content_byte;
    }

    public String getCourse_detail_content_assessment() {
        return course_detail_content_assessment;
    }

    public void setCourse_detail_content_assessment(String course_detail_content_assessment) {
        this.course_detail_content_assessment = course_detail_content_assessment;
    }

    public String getCourse_detail_id() {
        return course_detail_id;
    }

    public void setCourse_detail_id(String course_detail_id) {
        this.course_detail_id = course_detail_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_detail_name() {
        return course_detail_name;
    }

    public void setCourse_detail_name(String course_detail_name) {
        this.course_detail_name = course_detail_name;
    }

    public String getCourse_detail_type() {
        return course_detail_type;
    }

    public void setCourse_detail_type(String course_detail_type) {
        this.course_detail_type = course_detail_type;
    }

    public String getCourse_detail_content_text() {
        return course_detail_content_text;
    }

    public void setCourse_detail_content_text(String course_detail_content_text) {
        this.course_detail_content_text = course_detail_content_text;
    }

    public String getIs_subscription() {
        return is_subscription;
    }

    public void setIs_subscription(String is_subscription) {
        this.is_subscription = is_subscription;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }
}

package com.jagoit.JagoITBE.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "COURSE")
public class Course {

    @Id
    @Column(name = "COURSE_ID")
    private String course_id;

    @Column(name = "CATEGORY_ID")
    private String category_id;

    @Column(name = "COURSE_NAME")
    private String course_name;

    @Column(name = "CREATED_DATE")
    private Timestamp created_date;

    @Column(name = "COURSE_PICTURE")
    private byte[] course_picture;

    @Column(name = "COUNT")
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public byte[] getCourse_picture() {
        return course_picture;
    }

    public void setCourse_picture(byte[] course_picture) {
        this.course_picture = course_picture;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }
}

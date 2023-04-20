package com.jagoit.JagoITBE.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@IdClass(CourseActivity.class)
@Table(name = "user_course_activity")
public class UserCourseActivity {

    @Id
    @Column(name = "USER_ID")
    private String user_id;

    @Id
    @Column(name = "COURSE_ID")
    private String course_id;

    @Id
    @Column(name = "COURSE_DETAIL_ID")
    private String course_detail_id;

    @Column(name = "CREATED_DATE")
    private Timestamp created_date;

    @Column(name = "SCORE")
    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_detail_id() {
        return course_detail_id;
    }

    public void setCourse_detail_id(String course_detail_id) {
        this.course_detail_id = course_detail_id;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }
}

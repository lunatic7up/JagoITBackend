package com.jagoit.JagoITBE.service.impl;

import com.jagoit.JagoITBE.model.*;
import com.jagoit.JagoITBE.repository.CourseDetailRepository;
import com.jagoit.JagoITBE.repository.CourseRepository;
import com.jagoit.JagoITBE.repository.UserCourseActivityRepository;
import com.jagoit.JagoITBE.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.id.UUIDHexGenerator;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LogManager.getLogger(CourseServiceImpl.class);


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Autowired
    private UserCourseActivityRepository userCourseActivityRepository;

    @Override
    public List<Course> getAllCourseByCategoryId(String categoryID) {
        logger.debug(">> getAllCourseByCategoryId");
        return courseRepository.getAllCourseByCategoryId(categoryID);
    }

    @Override
    public List<CourseDetail> getCourseDetailByCourseId(String courseID) {
        logger.debug(">> getCourseDetailByCourseId");
        return courseDetailRepository.getCourseDetailByCourseId(courseID);
    }

    @Override
    public UserCourseActivity insertUserCourseActivity(Map<String, Object> paramMap) {
        logger.debug(">> insertUserCourseActivity");
        String course_id = paramMap.get("course_id").toString();
        String user_id = paramMap.get("user_id").toString();
        String course_detail_id = paramMap.get("course_detail_id").toString();
        UserCourseActivity userCourseActivity = new UserCourseActivity();
        userCourseActivity.setUser_id(user_id);
        userCourseActivity.setCourse_id(course_id);
        userCourseActivity.setCourse_detail_id(course_detail_id);
        userCourseActivity.setScore(Double.parseDouble(paramMap.get("score").toString()));
        userCourseActivity.setCreated_date(new Timestamp(System.currentTimeMillis()));
        return userCourseActivityRepository.save(userCourseActivity);
    }

    @Override
    public List<UserCourseActivity> getUserCourseActivity(String user_id, String course_id) {
        logger.debug(">> getUserCourseActivity");
        return userCourseActivityRepository.getUserCourseActivity(user_id, course_id);
    }

    @Override
    public Course updateCourseViewCount(String course_id) {
        logger.debug(">> updateCourseViewCount");
        Course course = courseRepository.getById(course_id);
        course.setCount(course.getCount() + 1);
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourseDetail(String course_detail_id) {
        logger.debug(">> deleteCourseDetail");
        this.courseDetailRepository.deleteById(course_detail_id);
    }

    @Override
    public void deleteUserActivityDetail(String course_detail_id) {
        logger.debug(">> deleteUserActivityDetail");
        this.userCourseActivityRepository.deleteUserActivity(course_detail_id);
    }

    @Override
    public CourseDetail getCourseDetailById(String course_detail_id) {
        logger.debug(">> getCourseDetailById");
        return courseDetailRepository.getById(course_detail_id);
    }

    @Override
    public UserCourseActivity updateScore(Map<String, Object> paramMap) {
        logger.debug(">> updateScore");
        String user_id = paramMap.get("user_id").toString();
        String course_id = paramMap.get("course_id").toString();
        String course_detail_id = paramMap.get("course_detail_id").toString();
        UserCourseActivity userCourseActivity = userCourseActivityRepository.getUserCourseActivityDetail(user_id, course_id, course_detail_id);
        userCourseActivity.setScore(Double.parseDouble(paramMap.get("score").toString()));
        return userCourseActivityRepository.save(userCourseActivity);
    }

    @Override
    public Course insertCourse(Map<String, Object> paramMap) {
        logger.debug(">> insertCourse");
        Course course = new Course();

//        List<Course> list = courseRepository.findAll();
//        String size = Integer.toString(list.size() + 1);
//        String id = "CS";
//        while(size.length() != 3){
//            size = "0" + size;
//        }
//        id = id + size;

        course.setCourse_id(new UUIDHexGenerator().generate(null, null).toString());
        course.setCourse_name(paramMap.get("course_name").toString());
        course.setCategory_id(paramMap.get("category_id").toString());
        byte[] imageBytes = Base64.decodeBase64(paramMap.get("course_picture").toString());
        course.setCourse_picture(imageBytes);
        course.setCount(0);

        course.setCreated_date(new Timestamp(System.currentTimeMillis()));


        return this.courseRepository.save(course);
    }

    @Override
    public CourseDetail insertCourseDetail(Map<String, Object> paramMap) throws JSONException {
        logger.debug(">> insertCourseDetail");
        CourseDetail courseDetail = new CourseDetail();
//        List<CourseDetail> list = courseDetailRepository.findAll();
//        logger.debug("size: " + list.size());
//        String size = Integer.toString(list.size() + 1);
//        logger.debug("size: " + size);
//        String id = "CD";
//        while(size.length() != 5){
//            size = "0" + size;
//        }
//        id = id + size;

        courseDetail.setCourse_detail_id(new UUIDHexGenerator().generate(null, null).toString());
        courseDetail.setCourse_detail_name(paramMap.get("course_detail_name").toString());
        courseDetail.setCourse_detail_type(paramMap.get("course_detail_type").toString());
        courseDetail.setCourse_id(paramMap.get("course_id").toString());
        courseDetail.setCourse_detail_desc(paramMap.get("course_detail_desc").toString());
        if(paramMap.get("course_detail_type").toString().equalsIgnoreCase("PDF")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("course_detail_content").toString());
            courseDetail.setCourse_detail_content_byte(imageBytes);
            courseDetail.setCourse_detail_content_assessment(paramMap.get("course_detail_assessment").toString());
        }else if(paramMap.get("course_detail_type").toString().equalsIgnoreCase("VIDEO")){
            courseDetail.setCourse_detail_content_text(paramMap.get("course_detail_content").toString());
            courseDetail.setCourse_detail_content_assessment(paramMap.get("course_detail_assessment").toString());
        }else{
            courseDetail.setCourse_detail_content_assessment(paramMap.get("course_detail_assessment").toString());
        }

        courseDetail.setIs_subscription(paramMap.get("is_subscription").toString());
        courseDetail.setCreated_date(new Timestamp(System.currentTimeMillis()));
        courseDetail.setUpdated_date(new Timestamp(System.currentTimeMillis()));

        return courseDetailRepository.save(courseDetail);
    }

    @Override
    public CourseDetail editCourseDetail(Map<String, Object> paramMap) throws JSONException {
        logger.debug(">> editCourseDetail");
        CourseDetail courseDetail = courseDetailRepository.getById(paramMap.get("course_detail_id").toString());
        courseDetail.setCourse_detail_name(paramMap.get("course_detail_name").toString());
        courseDetail.setCourse_detail_type(paramMap.get("course_detail_type").toString());
        if(paramMap.get("course_detail_type").toString().equalsIgnoreCase("PDF")){
            byte[] imageBytes = Base64.decodeBase64(paramMap.get("course_detail_content").toString());
            courseDetail.setCourse_detail_content_byte(imageBytes);
            courseDetail.setCourse_detail_content_assessment(paramMap.get("course_detail_assessment").toString());
            courseDetail.setCourse_detail_content_text(null);
        }else if(paramMap.get("course_detail_type").toString().equalsIgnoreCase("VIDEO")){
            courseDetail.setCourse_detail_content_text(paramMap.get("course_detail_content").toString());
            courseDetail.setCourse_detail_content_assessment(paramMap.get("course_detail_assessment").toString());
            courseDetail.setCourse_detail_content_byte(null);
        }else{
            courseDetail.setCourse_detail_content_text(null);
            courseDetail.setCourse_detail_content_byte(null);
            courseDetail.setCourse_detail_content_assessment(paramMap.get("course_detail_assessment").toString());
        }

        courseDetail.setIs_subscription(paramMap.get("is_subscription").toString());
        courseDetail.setUpdated_date(new Timestamp(System.currentTimeMillis()));
        return courseDetailRepository.save(courseDetail);
    }
}

package com.jagoit.JagoITBE.service;

import com.jagoit.JagoITBE.model.Course;
import com.jagoit.JagoITBE.model.CourseDetail;
import com.jagoit.JagoITBE.model.UserCourseActivity;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public interface CourseService {

    List<Course> getAllCourseByCategoryId(String categoryID);

    Course insertCourse(Map<String, Object> paramMap);

    CourseDetail insertCourseDetail(Map<String, Object> paramMap) throws JSONException;

    CourseDetail editCourseDetail(Map<String, Object> paramMap) throws JSONException;

    List<CourseDetail> getCourseDetailByCourseId(String courseID);

    UserCourseActivity insertUserCourseActivity(Map<String, Object> paramMap);

    List<UserCourseActivity> getUserCourseActivity(String user_id, String course_id);

    Course updateCourseViewCount(String course_id);

    void deleteCourseDetail(String course_detail_id);

    void deleteUserActivityDetail(String course_detail_id);

    CourseDetail getCourseDetailById(String course_detail_id);

    UserCourseActivity updateScore(Map<String, Object> paramMap);
}

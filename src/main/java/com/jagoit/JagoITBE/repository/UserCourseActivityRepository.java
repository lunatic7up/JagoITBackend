package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.model.CourseActivity;
import com.jagoit.JagoITBE.model.UserCourseActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserCourseActivityRepository extends JpaRepository<UserCourseActivity, CourseActivity> {

    @Query(value = "SELECT * FROM USER_COURSE_ACTIVITY WHERE USER_ID=?1 AND COURSE_ID=?2 ORDER BY CREATED_DATE ASC", nativeQuery = true)
    List<UserCourseActivity> getUserCourseActivity(String user_id, String course_id);

    @Query(value = "SELECT * FROM USER_COURSE_ACTIVITY WHERE USER_ID=?1 AND COURSE_ID=?2 AND COURSE_DETAIL_ID=?3", nativeQuery = true)
    UserCourseActivity getUserCourseActivityDetail(String user_id, String course_id, String course_detail_id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM USER_COURSE_ACTIVITY WHERE COURSE_DETAIL_ID=?1", nativeQuery = true)
    void deleteUserActivity(String course_detail_id);

}

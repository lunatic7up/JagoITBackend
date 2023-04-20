package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.model.CourseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDetailRepository extends JpaRepository<CourseDetail, String> {

    @Query(value = "SELECT * FROM COURSE_DETAIL WHERE COURSE_ID=?1 ORDER BY CREATED_DATE ASC", nativeQuery = true)
    List<CourseDetail> getCourseDetailByCourseId(String courseID);

}

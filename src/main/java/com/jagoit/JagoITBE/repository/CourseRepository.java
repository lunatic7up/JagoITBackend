package com.jagoit.JagoITBE.repository;

import com.jagoit.JagoITBE.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query(value = "SELECT * FROM COURSE WHERE CATEGORY_ID=?1 ORDER BY COUNT DESC", nativeQuery = true)
    List<Course> getAllCourseByCategoryId(String categoryID);

}

package com.alarm.parent.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by dengb.
 */
@Mapper
@Component
public interface CourseMapper {
    int countCourseById(long id);

}

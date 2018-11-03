package com.alarm.parent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alarm.parent.config.GroupPurchaseConfig;
import com.alarm.parent.mybatis.dao.CourseMapper;
import com.alarm.parent.mybatis.service.CourseService;

/**
 * Created by dengb.
 */
@RestController
@RequestMapping(value = "/course")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    CourseService courseService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    GroupPurchaseConfig gpConfig;

    /**
     * 返回课程拼团信息
     *详情见文档
     *
     * @param id
     * @param unionid
     * @return
     */
    @RequestMapping(value = "/instance", method = RequestMethod.GET)
    public @ResponseBody Object getCourse(@RequestParam(required = false, value = "id") Long id,
                                          @RequestParam(required = false, value = "unionid") String unionid) {
        logger.info("Course instance, id: " + id + ", unionid: " + unionid);
        return "test OK";
    }

}

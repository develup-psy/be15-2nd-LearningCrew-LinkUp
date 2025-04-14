package com.learningcrew.linkup.notification.query.mapper;

import com.learningcrew.linkup.notification.query.dto.response.NotificationResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper {
    List<NotificationResponse> findRecentNotificationsByUserId(@Param("userId") Integer userId);
}
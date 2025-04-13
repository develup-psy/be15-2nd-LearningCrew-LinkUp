package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserMeetingHistoryDto;

import java.util.List;

public interface MeetingQueryService {

    List<UserMeetingHistoryDto> findMeetingsByUser(int userId);
}


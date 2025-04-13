package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserMeetingDto;

import java.util.List;

public interface MeetingQueryService {

    List<UserMeetingDto> findMeetingsByUser(int userId);
}


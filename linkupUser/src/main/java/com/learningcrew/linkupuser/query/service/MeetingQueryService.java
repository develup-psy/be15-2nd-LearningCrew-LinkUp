package com.learningcrew.linkupuser.query.service;


import com.learningcrew.linkupuser.query.dto.query.UserMeetingDto;

import java.util.List;

public interface MeetingQueryService {

    List<UserMeetingDto> findMeetingsByUser(int userId);
}


package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserMeetingHistoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Service;

import java.util.List;

@Schema
@Service
public class MeetingQueryServiceImpl implements MeetingQueryService {
    @Override
    public List<UserMeetingHistoryDto> findMeetingsByUser(Long userId) {
        return List.of();
    }
}

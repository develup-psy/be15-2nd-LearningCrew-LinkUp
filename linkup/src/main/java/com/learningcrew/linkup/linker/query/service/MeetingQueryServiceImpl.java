package com.learningcrew.linkup.linker.query.service;

import com.learningcrew.linkup.linker.query.dto.query.UserMeetingDto;
import com.learningcrew.linkup.linker.query.mapper.UserMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Schema
@Service
@RequiredArgsConstructor
public class MeetingQueryServiceImpl implements MeetingQueryService {
    private final UserMapper userMapper;

    /* 참가 모임 조회 */
    @Override
    @Transactional(readOnly = true)
    public List<UserMeetingDto> findMeetingsByUser(int userId) {
        return userMapper.findUserMeetings(userId);
    }
}

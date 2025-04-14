package com.learningcrew.linkup.meeting.query.mapper;

import com.learningcrew.linkup.meeting.query.dto.request.MeetingSearchRequest;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMeetingMapper {

    List<MeetingDTO> selectAllMeetings(MeetingSearchRequest request);

    long countAllMeetings(MeetingSearchRequest request);
}

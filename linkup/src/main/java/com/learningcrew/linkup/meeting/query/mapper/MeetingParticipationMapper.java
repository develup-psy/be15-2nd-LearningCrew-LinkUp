package com.learningcrew.linkup.meeting.query.mapper;

import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingParticipationMapper {
    /* 모임 ID, 회원 ID로 참가 내역 하나 조회 */
    MeetingParticipationDTO selectMeetingParticipationByMeetingIdAndMemberId(int meetingId, int memberId);

    /* 모임 ID로 참가한 회원 조회 */
    List<MemberDTO> selectParticipantsByMeetingId(int meetingId);

}

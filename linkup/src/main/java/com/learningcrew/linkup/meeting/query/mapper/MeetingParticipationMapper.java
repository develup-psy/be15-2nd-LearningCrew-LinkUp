package com.learningcrew.linkup.meeting.query.mapper;

import com.learningcrew.linkup.meeting.query.dto.response.MeetingDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MeetingParticipationMapper {
    /* 모임 ID, 회원 ID로 참가 내역 하나 조회 (ACCEPTED 상태) */
    MeetingParticipationDTO selectMeetingParticipationByMeetingIdAndMemberId(int meetingId, int memberId);

    /* 모임 ID, 회원 ID로 참가 내역 하나 조회 (status 무관) */
    MeetingParticipationDTO selectHistoryByMeetingIdAndMemberId(int meetingId, int memberId);

    /* 모임 ID로 참가한 회원 목록 조회 */
    List<MemberDTO> selectParticipantsByMeetingId(int meetingId);

    List<MeetingParticipationDTO> selectHistoryByMeetingIdAndStatusId(int meetingId, int statusId);

    // 특정 회원의 전체 모임 참여 이력 조회 (관리자용)
    List<MeetingDTO> selectPastMeetingsByUserId(@Param("userId") int userId, @Param("now") LocalDate now);

    // 특정 회원의 예정된 모임 조회 (관리자용)
    List<MeetingDTO> selectUpcomingMeetingsByUserId(@Param("userId") int userId, @Param("now") LocalDate now);

    // 회원 본인의 참여 이력 조회 (회원용)
    List<MeetingParticipationDTO> selectMeetingParticipationsByUserId(int userId);


    /* 모임 ID와 status로 참가 내역 목록 조회 */
    List<MeetingParticipationDTO> selectHistoriesByMeetingIdAndStatusId(int meetingId, int statusId);
}

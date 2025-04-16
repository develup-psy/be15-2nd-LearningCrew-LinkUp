package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.common.infrastructure.MemberQueryClient;
import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationDTO;
import com.learningcrew.linkup.meeting.query.dto.response.MeetingParticipationResponse;
import com.learningcrew.linkup.meeting.query.dto.response.MemberDTO;
import com.learningcrew.linkup.meeting.query.mapper.MeetingParticipationMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingParticipationQueryService {

    private final MeetingParticipationMapper mapper;
    private final StatusQueryService statusQueryService;
    private final ModelMapper modelMapper;
    private final MemberQueryClient memberQueryClient;

    /* 모임에 속한 참가자 전체 조회 */
    @Transactional(readOnly = true)
    public List<MemberDTO> getParticipantsByMeetingId(int meetingId) {
        int statusId = statusQueryService.getStatusId("ACCEPTED");
        List<MeetingParticipationDTO> histories = getHistories(meetingId, statusId);

        List<Integer> memberIds = histories.stream()
                .map(MeetingParticipationDTO::getMemberId)
                .toList();

        /* MSA 분리 시 멤버에서 호출해오기  -> 수정 필요(N+1) 문제*/
        return memberIds.stream()
                .map(id -> memberQueryClient.getMemberById(id).getData())
                .map(x -> modelMapper.map(x, MemberDTO.class))
                .toList();
    }

    /* 모임에 속한 참가 내역 status별 조회 */
    @Transactional(readOnly = true)
    public List<MeetingParticipationDTO> getHistories(int meetingId, int statusId) {
        return mapper.selectHistoriesByMeetingIdAndStatusId(meetingId, statusId);
    }

    /* 참가자 목록 조회 -> 30명이 최대이므로 따로 페이징 처리 안함 */
    @Transactional(readOnly = true)
    public List<MemberDTO> getParticipantsOfMyMeeting(int meetingId, int memberId) {
        // 1. 해당 모임에 속해있는지 확인
        List<MemberDTO> participants = getParticipantsByMeetingId(meetingId);

        boolean isParticipant = participants.stream()
                .anyMatch(participant -> participant.getMemberId() == memberId);

        if (!isParticipant) {
            throw new BusinessException(ErrorCode.FORBIDDEN); // 조회 권한 없음
        }

        // 2. 있으면 조회
        return participants;
    }


    @Transactional(readOnly = true)
    public List<MemberDTO> getParticipants(int meetingId, int memberId) {
        List<MemberDTO> response = mapper.selectParticipantsByMeetingId(meetingId);
        MeetingParticipationDTO participation = mapper.selectMeetingParticipationByMeetingIdAndMemberId(meetingId, memberId);

        if (participation == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "해당 모임에 존재하지 않는 회원입니다.");
        }

        return response;
    }

    @Transactional(readOnly = true)
    public MeetingParticipationResponse getParticipation(int meetingId, int memberId) {
        MeetingParticipationDTO response = mapper.selectMeetingParticipationByMeetingIdAndMemberId(meetingId, memberId);

        return MeetingParticipationResponse.builder()
                .participationId(response.getParticipationId())
                .build();
    }

    public List<MeetingParticipationHistory> getAcceptedParticipants(int meetingId) {
        return null;
    }
}

package com.learningcrew.linkup.meeting.query.service;

import com.learningcrew.linkup.exception.BusinessException;
import com.learningcrew.linkup.exception.ErrorCode;
import com.learningcrew.linkup.linker.command.domain.aggregate.Member;
import com.learningcrew.linkup.linker.command.domain.repository.MemberRepository;
import com.learningcrew.linkup.meeting.command.domain.aggregate.MeetingParticipationHistory;
import com.learningcrew.linkup.meeting.command.domain.repository.MeetingParticipationHistoryRepository;
import com.learningcrew.linkup.meeting.command.infrastructure.repository.JpaMeetingParticipationHistoryRepository;
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
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final MeetingParticipationHistoryRepository meetingParticipationHistoryRepository;

    /* 모임에 속한 참가자 전체 조회 */
    @Transactional(readOnly = true)
    public List<MemberDTO> getParticipantsByMeetingId(int meetingId) {
        int statusId = statusQueryService.getStatusId("ACCEPTED");
        List<MeetingParticipationDTO> histories = getHistories(meetingId, statusId);

        List<Integer> memberIds = histories.stream()
                .map(MeetingParticipationDTO::getMemberId)
                .toList();

        /* MSA 분리 시 멤버에서 호출해오기 */
        return memberIds.stream()
                .map(id -> memberRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND)))
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
        // 1. 해당 모임의 승인된 참가 내역 조회
        List<MeetingParticipationHistory> history = meetingParticipationHistoryRepository
                .findAllByMeetingIdAndStatusId(meetingId, statusQueryService.getStatusId("ACCEPTED"));

        // 2. 참가자 목록 반환 및 해당 회원이 있는지 체크
        List<Member> members = memberRepository.findAllById(
                history.stream()
                        .map(MeetingParticipationHistory::getMemberId)
                        .toList()
        );

        List<MemberDTO> participants = members.stream()
                .map(member -> modelMapper.map(member, MemberDTO.class))
                .toList();

        boolean isParticipated = history.stream().anyMatch(x -> x.getMemberId() == memberId);

        if (!isParticipated) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "해당 모임에 존재하지 않는 회원입니다.");
        }

        return participants;
    }

    @Transactional(readOnly = true)
    public MeetingParticipationResponse getParticipation(int meetingId, int memberId) {
        MeetingParticipationDTO response = mapper.selectMeetingParticipationByMeetingIdAndMemberId(meetingId, memberId);

        if (response == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "참가 이력이 존재하지 않습니다.");
        }

        return MeetingParticipationResponse.builder()
                .participationId(response.getParticipationId())
                .build();
    }

    @Transactional(readOnly = true)
    public MeetingParticipationListResponse getUserMeetingHistory(int userId) {
        List<MeetingParticipationDTO> participations = mapper.selectMeetingParticipationsByUserId(userId);

        return MeetingParticipationListResponse.builder()
                .meetingParticipations(participations)
                .build();
    }
}

package com.learningcrew.linkup.meeting.command.application.service;

import com.learningcrew.linkup.meeting.command.application.dto.request.InterestedMeetingCommandRequest;
import com.learningcrew.linkup.meeting.command.domain.aggregate.InterestedMeeting;
import com.learningcrew.linkup.meeting.query.dto.response.InterestedMeetingDTO;
import com.learningcrew.linkup.meeting.command.domain.repository.InterestedMeetingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestedMeetingCommandService {

    private final InterestedMeetingRepository repository;
    private final ModelMapper modelMapper;

    /* 모임 찜 등록 */
    @Transactional
    public int createInterestedMeeting(InterestedMeetingCommandRequest request) {
        InterestedMeeting interestedMeeting = modelMapper.map(request, InterestedMeeting.class);

        repository.save(interestedMeeting);

        return interestedMeeting.getMeeting().getMeetingId();
    }

    /* 모임 찜 취소 */
    @Transactional
    public void deleteInterestedMeeting(InterestedMeetingCommandRequest request) {
        InterestedMeeting deleteInterestedMeeting = modelMapper.map(request, InterestedMeeting.class);

        repository.delete(deleteInterestedMeeting);
    }

}

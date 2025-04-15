package com.learningcrew.linkup.meeting.command.domain.repository;

import com.learningcrew.linkup.meeting.command.domain.aggregate.Meeting;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeetingRepository {
    Meeting save(Meeting meeting);

    Optional<Meeting> findById(int meetingId);

    void flush();

//    @Query("SELECT m FROM Meeting m WHERE m.startTime <= :now AND m.statusId NOT IN :excludedStatusIds")
//    List<Meeting> findMeetingsToSettle(@Param("now") LocalDateTime now, @Param("excludedStatusIds") List<Integer> excludedStatusIds);


}

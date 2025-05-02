package com.learningcrew.linkup.place.command.domain.repository;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Reservation;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("""
    SELECT COUNT(r) FROM Reservation r
    WHERE r.reservationDate = :date
      AND r.placeId = :placeId
      AND (
        (r.startTime < :endTime AND r.endTime > :startTime)
      )
    """)
    int countConflictingReservations(
            @Param("placeId") int placeId,
            @Param("date") Date date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    Optional<Reservation> findByMeetingId(int meetingId);


}


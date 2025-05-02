package com.learningcrew.linkup.place.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "reservation")
@NoArgsConstructor()
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private int reservationId;

    @Column(name = "place_id")
    private int placeId;

    @Column(name = "meeting_id")
    private int meetingId;

    @Column(name = "status_id")
    private int statusId;

    @Temporal(TemporalType.DATE)
    @Column(name = "reservation_date")
    private Date reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
}

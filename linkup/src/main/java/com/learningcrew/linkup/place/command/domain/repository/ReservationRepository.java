package com.learningcrew.linkup.place.command.domain.repository;
import com.learningcrew.linkup.place.command.domain.aggregate.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}

package com.learningcrew.linkupuser.command.domain.repository;


import com.learningcrew.linkupuser.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByUserName(String email);

    Optional<User> findByEmail(String email);

    boolean existsByContactNumber(String contactNumber);
}

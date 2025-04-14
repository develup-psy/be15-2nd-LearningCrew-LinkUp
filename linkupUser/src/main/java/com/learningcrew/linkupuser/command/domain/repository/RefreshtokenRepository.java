package com.learningcrew.linkupuser.command.domain.repository;

import com.learningcrew.linkupuser.command.domain.aggregate.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshtokenRepository extends JpaRepository<RefreshToken, Integer> {

}

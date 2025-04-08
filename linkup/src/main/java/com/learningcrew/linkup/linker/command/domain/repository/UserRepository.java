package com.learningcrew.linkup.linker.command.domain.repository;

import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByUserName(@NotBlank(message = "이메일을 입력해주세요") @Email(message = "이메일 형식으로 작성해주세요") String email);

    Optional<User> findByEmail(String email);
}

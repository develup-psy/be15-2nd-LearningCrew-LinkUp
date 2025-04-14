package com.learningcrew.linkup.point.command.domain.repository;

import com.learningcrew.linkup.point.command.domain.aggregate.Account;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserId(int userId);
    boolean existsByUserId(int userId);

    boolean existsByAccountNumber(@NotBlank(message = "계좌번호는 필수입니다. ") String accountNumber);
}
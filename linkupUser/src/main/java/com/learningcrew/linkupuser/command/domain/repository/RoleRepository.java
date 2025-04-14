package com.learningcrew.linkupuser.command.domain.repository;

import com.learningcrew.linkupuser.common.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}

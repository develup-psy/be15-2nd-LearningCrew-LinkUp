package com.learningcrew.linkup.point.query.mapper;

import com.learningcrew.linkup.point.query.dto.query.AccountFindDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AccountMapper {
    Optional<AccountFindDto> findByUserId(int userId);
}

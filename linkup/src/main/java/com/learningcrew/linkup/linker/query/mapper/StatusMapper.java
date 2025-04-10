package com.learningcrew.linkup.linker.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StatusMapper {
    @Select("SELECT role_id from ROLE WHERE role_name = #{roleName}")
    int roleIdByRoleName(String roleName);
}

package com.lagou.mapper;

import com.lagou.domain.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {
    @Select("select * from user_role ur inner join role r on ur.role_id=r.id where ur.user_id=#{uid}")
    List<Role> findByUid(Integer uid);
}

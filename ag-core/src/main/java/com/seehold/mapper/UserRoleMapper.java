package com.seehold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seehold.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    void deleteByUserId(@Param("userId") Long userId);

    void insertUserRole(@Param("userId") Long userId,
                        @Param("roleId") Long roleId,
                        @Param("grantedBy") String grantedBy);
}
package com.itlike.mapper;

import com.itlike.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long rid);

    int insert(Role record);

    Role selectByPrimaryKey(Long rid);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    void insertRoleAndPermissionRel(@Param("rid") Long rid, @Param("pid")Long pid);

    void deleteRoleAndPermissionRel(Long rid);

    List<Long> getRoleWithId(Long id);
}
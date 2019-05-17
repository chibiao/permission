package com.itlike.mapper;

import com.itlike.domain.Employee;
import com.itlike.domain.QueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    List<Employee> selectAll(QueryVo vo);

    int updateByPrimaryKey(Employee record);

    void updateState(Long id);
    /*建立员工和角色的关系*/
    void insertEmployeeAndRoleRel(@Param("id") Long id, @Param("rid") Long rid);
    /*打破员工和角色的关系*/
    void deleteEmployeeAndRoleRel(Long id);
    /*根据用户名查询有没有当前用户*/
    Employee getEmployeeWithUserName(String username);
    /*根据用户的id查询角色编号名称*/
    List<String> getRolesById(Long id);
    /*根据当前用户id,查询角色和权限*/
    List<String> getPermissionById(Long id);
}
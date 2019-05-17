package com.itlike.service;

import com.itlike.domain.Employee;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;

import java.util.List;

public interface EmployeeService {
    /*查询员工*/
    public PageListRes getEmployee(QueryVo vo);

    /*保存员工*/
    void saveEmployee(Employee employee);

    /*更新员工*/
    void updataEmployee(Employee employee);

    /*更新离职状态*/
    void updateState(Long id);

    Employee getEmployeeWithUserName(String username);
    /*根据用户的id查询角色编号名称*/
    List<String> getRolesById(Long id);
    /*根据当前用户id,查询角色和权限*/
    List<String> getPermissionById(Long id);

}

package com.itlike.web.realm;

import com.itlike.domain.Employee;
import com.itlike.service.EmployeeService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRealm extends AuthorizingRealm {

    @Autowired
    private EmployeeService employeeService;

    /*授权*/
    /*
     * web doGetAuthorizationInfo什么时候调用
     * 1.发现访问路径对应的方法上面有授权的注解 就会调用doGetAuthorizationInfo
     * 2.页面当中有授权标签,也会调用doGetAuthorizationInfo
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权调用---");
        /*获取用户的认证信息*/
        Employee employee = (Employee) principals.getPrimaryPrincipal();
        /*根据当前用户id,查询角色和权限*/
        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        if (employee.getAdmin()) {
            permissions.add("*:*");
        } else {
            /*查询角色*/
            roles = employeeService.getRolesById(employee.getId());
            /*查询权限*/
            permissions = employeeService.getPermissionById(employee.getId());
        }
        /*给授权信息*/
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(permissions);
        return info;
    }

    /*认证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        /*到数据库中查询有没有当前用户*/
        /*根据用户名查询有没有当前用户*/
        Employee employee = employeeService.getEmployeeWithUserName(username);
        if (employee == null) {
            return null;
        }
        /*认证*/
        /*参数 主体,正确的密码,盐,当前realm的名称*/
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(employee,
                employee.getPassword(),
                ByteSource.Util.bytes(employee.getUsername()),
                this.getName());
        return info;
    }
}

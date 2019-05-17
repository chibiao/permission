package com.itlike.web;

import com.itlike.domain.Permission;
import com.itlike.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @RequestMapping("/permissionList")
    @ResponseBody
    public List<Permission> permissionList(){
        return permissionService.getPermissions();
    }
    @RequestMapping("/getPermissionByRid")
    @ResponseBody
    public List<Permission> getPermissionByRid(Long rid){
        List<Permission> permissions = permissionService.getPermissionByRid(rid);
        return permissions;
    }
}

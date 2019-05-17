package com.itlike.web;

import com.itlike.domain.AjaxRes;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.domain.Role;
import com.itlike.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;
    @RequestMapping("/role")
    public String role(){
        return "role";
    }

    @RequestMapping("/getRoles")
    @ResponseBody
    public PageListRes getRoles(QueryVo vo){
        PageListRes roles = roleService.getRoles(vo);
        return roles;
    }
    /*保存角色*/
    @RequestMapping("/saveRole")
    @ResponseBody
    public AjaxRes saveRole(Role role){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            roleService.saveRole(role);
            ajaxRes.setMsg("保存成功!");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("保存失败!");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*更新角色*/
    @RequestMapping("/updateRole")
    @ResponseBody
    public AjaxRes updateRole(Role role){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            roleService.updateRole(role);
            ajaxRes.setMsg("更新角色成功!");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("更新角色失败!");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*删除角色*/
    @RequestMapping("/deleteRole")
    @ResponseBody
    public AjaxRes updateRole(long rid){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            roleService.deleteRole(rid);
            ajaxRes.setMsg("删除角色成功!");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("删除角色失败!");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*查询所有角色*/
    @RequestMapping("/roleList")
    @ResponseBody
    public List<Role> roleList(){
        return roleService.roleList();
    }

    /*根据员工编号查询角色编号*/
    @RequestMapping("/getRoleByEid")
    @ResponseBody
    public List<Long> getRoleByEid(Long id){
        /*查询对应的角色*/
        return roleService.getRoleByEid(id);
    }
}

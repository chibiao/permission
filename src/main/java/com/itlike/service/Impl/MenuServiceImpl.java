package com.itlike.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itlike.domain.*;
import com.itlike.mapper.MenuMapper;
import com.itlike.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public PageListRes getMenuList(QueryVo vo) {
        Page<Object> page = PageHelper.startPage(vo.getPage(), vo.getRows());
        List<Menu> menus = menuMapper.selectAll();
        /*封装成pageList*/
        PageListRes pageListRes=new PageListRes();
        pageListRes.setRows(menus);
        pageListRes.setTotal(page.getTotal());
        return pageListRes;
    }

    @Override
    public List<Menu> parentList() {
        return menuMapper.selectAll();
    }
    /*接收菜单表单*/
    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public AjaxRes updateMenu(Menu menu) {
        AjaxRes ajaxRes=new AjaxRes();
        /*判断你选择的父菜单是不是你自己的子菜单*/
        /*先取出父级菜单的id*/
        Long id = menu.getParent().getId();
        while(id!=null){
            /*查询出该id对应的menu*/
            Long parentId = menuMapper.selectParentId(id);
            if(menu.getId()==parentId){
                ajaxRes.setMsg("不能设置自己的子菜单为父菜单");
                ajaxRes.setSuccess(false);
                return ajaxRes;
            }
            id=parentId;
        }
        try {
            menuMapper.updateByPrimaryKey(menu);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setMsg("保存失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    @Override
    public AjaxRes deleteMenu(Long id) {
        AjaxRes ajaxRes=new AjaxRes();
        try {
            /*1.打破菜单关系*/
            menuMapper.updateMenuRel(id);
            /*2.删除记录*/
            menuMapper.deleteByPrimaryKey(id);
            ajaxRes.setMsg("删除成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setMsg("删除失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*获取树形菜单数据*/
    @Override
    public List<Menu> getTreeData() {
        List<Menu> treeData = menuMapper.getTreeData();
        /*
        判断当前用户有没有对应的权限
        如果没有就从集合当中移除
        */
        /*获取用户 判断用户是否是管理员 是管理就不需要做判断*/
        Subject subject = SecurityUtils.getSubject();
        /*当前的用户*/
        Employee employee = (Employee)subject.getPrincipal();
        if (!employee.getAdmin()){
            /*做检验权限*/
            checkPermission(treeData);
        }
        return treeData;
    }

    public void checkPermission(List<Menu> menus){
        //获取主体
        Subject subject = SecurityUtils.getSubject();
        //遍历所有的菜单及子菜单
        Iterator<Menu> iterator = menus.iterator();
        while (iterator.hasNext()){
            Menu menu = iterator.next();
            if (menu.getPermission() !=null){
                //判断当前menu是否有权限对象,如果说没有 当前遍历的菜单从集合当中移除
                String presource = menu.getPermission().getPresource();
                if (!subject.isPermitted(presource)){
                    //当前遍历的菜单从集合当中移除
                    iterator.remove();
                    continue;
                }
            }
            /*判断是否有子菜单  有子菜单也要做权限检验*/
            if (menu.getChildren().size() > 0){
                checkPermission(menu.getChildren());
            }
        }
    }
}

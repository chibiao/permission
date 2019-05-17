package com.itlike.service;

import com.itlike.domain.AjaxRes;
import com.itlike.domain.Menu;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;

import java.util.List;

public interface MenuService {
    PageListRes getMenuList(QueryVo vo);

    List<Menu> parentList();

    void saveMenu(Menu menu);

    AjaxRes updateMenu(Menu menu);

    AjaxRes deleteMenu(Long id);
    /*获取树形菜单数据*/
    List<Menu> getTreeData();
}

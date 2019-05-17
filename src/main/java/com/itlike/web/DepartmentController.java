package com.itlike.web;

import com.itlike.domain.Department;
import com.itlike.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DepartmentController {
    /*注入servce*/
    @Autowired
    private DepartmentService departmentService;
    @RequestMapping("/departList")
    @ResponseBody
    public List<Department> departList(){
        return departmentService.getDepartmentList();
    }

}

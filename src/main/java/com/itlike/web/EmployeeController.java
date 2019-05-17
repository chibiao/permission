package com.itlike.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itlike.domain.AjaxRes;
import com.itlike.domain.Employee;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.service.EmployeeService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EmployeeController {
    @RequestMapping("/employee")
    @RequiresPermissions("employee:index")
    public String employee() {
        return "employee";
    }

    /*注入业务层*/
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/employeeList")
    @ResponseBody
    public PageListRes employeeList(QueryVo vo) {
        /*调用业务层查询员工*/
        return employeeService.getEmployee(vo);
    }

    /*接收员工表单*/
    @RequestMapping("/saveEmployee")
    @ResponseBody
    @RequiresPermissions("employee:add")
    public AjaxRes saveEmployee(Employee employee) {
        AjaxRes ajaxRes = new AjaxRes();
        try {
            employee.setState(true);
            employeeService.saveEmployee(employee);
            ajaxRes.setMsg("保存成功!");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("保存失败!");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    /*更新员工*/
    @RequestMapping("updateEmployee")
    @ResponseBody
    @RequiresPermissions("employee:edit")
    public AjaxRes updateEmployee(Employee employee) {
        AjaxRes ajaxRes = new AjaxRes();
        try {
            employee.setState(true);
            employeeService.updataEmployee(employee);
            ajaxRes.setMsg("修改成功!");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("修改失败!");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    /*更新离职状态*/
    @RequestMapping("/updateState")
    @ResponseBody
    @RequiresPermissions("employee:delete")
    public AjaxRes updateState(Long id) {
        AjaxRes ajaxRes = new AjaxRes();
        try {
            employeeService.updateState(id);
            ajaxRes.setMsg("修改成功!");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("修改失败!");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    /*处理shiro授权异常*/
    @ExceptionHandler(AuthorizationException.class)
    public void handleShiroException(HandlerMethod method, HttpServletResponse response) throws IOException {/*method 发生异常的方法*/
        /*跳转到一个页面 页面提示没有授权*/
        /*判断 当前请求是不是json请求 如果是 返回json给*/
        ResponseBody methodAnnotation = method.getMethodAnnotation(ResponseBody.class);
        if (methodAnnotation != null) {/*是json请求*/
            //ajax
            AjaxRes ajaxRes = new AjaxRes();
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("你没有权限操作");
            String s = new ObjectMapper().writeValueAsString(ajaxRes);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(s);
        } else {/*不是json请求*/
            response.sendRedirect("nopermission.jsp");
        }
    }

    /*下载员工数据*/
    @RequestMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response) {
        QueryVo queryVo = new QueryVo();
        queryVo.setPage(1);
        queryVo.setRows(10);
        //1.从数据库取出数据
        PageListRes plr = employeeService.getEmployee(queryVo);
        List<Employee> employees = (List<Employee>) plr.getRows();
        //2.创建excel
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("员工数据");
        //创建一行
        HSSFRow row = sheet.createRow(0);
        //创建一列
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("用户名");
        row.createCell(2).setCellValue("入职日期");
        row.createCell(3).setCellValue("电话");
        row.createCell(4).setCellValue("邮件");
        /*取出每一个员工设置数据*/
        HSSFRow employeeRow;
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            employeeRow = sheet.createRow(i + 1);
            employeeRow.createCell(0).setCellValue(employee.getId());
            employeeRow.createCell(1).setCellValue(employee.getUsername());
            if (employee.getInputtime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(employee.getInputtime());
                employeeRow.createCell(2).setCellValue(format);
            } else {
                employeeRow.createCell(2).setCellValue("");
            }
            employeeRow.createCell(3).setCellValue(employee.getTel());
            employeeRow.createCell(4).setCellValue(employee.getEmail());
        }
        //3.响应给浏览器
        try {
            String fileName = new String("员工数据.xls".getBytes(), "iso8859-1");
            response.setHeader("content-Disposition", "attachment;filename=" + fileName);
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*下载模版*/
    @RequestMapping("/downloadExcelTml")
    @ResponseBody
    public void downloadExcelTml(HttpServletRequest request, HttpServletResponse response) {
        FileInputStream fis = null;
        try {
            String fileName = new String("EmployeeTml.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition", "attachment;filename=" + fileName);
            /*获取文件*/
            //获取文件路径
            String realPath = request.getSession().getServletContext().getRealPath("static/excelTml.xls");
            fis = new FileInputStream(realPath);
            IOUtils.copy(fis, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*配置文件上传的解析器 mvc中配置*/
    @RequestMapping("/uploadExcelFile")
    @ResponseBody
    public AjaxRes uploadExcelFile(MultipartFile excel) {
        AjaxRes ajaxRes = new AjaxRes();
        try {
            HSSFWorkbook wb = new HSSFWorkbook(excel.getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            /*获取最大行号*/
            int lastRowNum = sheet.getLastRowNum();
            Row employeeRow=null;
            for (int i=1;i<=lastRowNum;i++){
                employeeRow=sheet.getRow(i);
                Employee employee=new Employee();
                employee.setUsername((String)getCellValue(employeeRow.getCell(1)));
                String s = (String)getCellValue(employeeRow.getCell(2));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(s);
                employee.setInputtime(date);
                employee.setTel((String)getCellValue(employeeRow.getCell(3)));
                employee.setEmail((String)getCellValue(employeeRow.getCell(4)));
                employee.setPassword("123456");
                employee.setState(true);
                employeeService.saveEmployee(employee);
            }
            ajaxRes.setSuccess(true);
            ajaxRes.setMsg("导入成功");
        } catch (Exception e) {
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("导入失败");
        }
        return ajaxRes;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
        }
        return cell;
    }
}

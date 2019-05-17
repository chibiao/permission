<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@include file="/static/common/common.jsp" %>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/role.js"></script>
    <style>
        #dialog #myform .panel-header {
            height: 25px;
        }

        #dialog #myform .panel-title {
            color: black;
            margin-top: -5px;
        }
    </style>
</head>
<body>
<%--工具栏--%>
<div id="toolbar">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" id="add">添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" id="edit">编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" id="remove">删除</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" id="reload">刷新</a>
</div>
<%--数据表格--%>
<table id="role_dg"></table>
<%--对话框--%>
<div id="dialog">
    <form id="myform">
        <table align="center" style="border-spacing: 20px 30px">
            <input type="hidden" name="rid">
            <tr align="center">
                <td>角色编号: <input type="text" name="rnum" class="easyui-validatebox" style="width: 120px"></td>
                <td>角色名称: <input type="text" name="rname" class="easyui-validatebox" style="width: 120px"></td>
            </tr>
            <tr>
                <td>
                    <div id="role_data1"></div>
                </td>
                <td>
                    <div id="role_data2"></div>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>

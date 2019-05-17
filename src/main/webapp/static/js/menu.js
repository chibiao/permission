$(function () {
    $("#menu_datagrid").datagrid({
        url: "/menuList",
        columns: [[
            {field: 'text', title: '名称', width: 100, align: 'center'},
            {field: 'url', title: '跳转地址', width: 100, align: 'center'},
            {
                field: 'parent', title: '父菜单', width: 100, align: 'center', formatter: function (value, row, index) {
                    return value ? value.text : '';
                }
            }
        ]],
        fit: true,
        rownumbers: true,
        singleSelect: true,
        striped: true,
        pagination: true,
        fitColumns: true,
        toolbar: '#menu_toolbar'
    });

    /*
     * 初始化新增/编辑对话框
     */
    $("#menu_dialog").dialog({
        width: 300,
        height: 300,
        closed: true,
        buttons: '#menu_dialog_bt'
    });
    /*监听添加按钮的点击*/
    $("#add").click(function () {
        $("#menu_dialog").dialog("open");
        $("#menu_dialog").dialog("setTitle", "添加菜单");
        $("#menu_dialog").form("clear");
    });
    /*监听编辑按钮的点击*/
    $("#edit").click(function () {
        $("#menu_form").form("clear");
        /*获取当前选中的行*/
        var rowData = $("#menu_datagrid").datagrid("getSelected");
        if (!rowData) {
            $.messager.alert("提示", "选择一行数据进行编辑");
            return;
        }
        if(rowData.parent){
            /*回显父级菜单*/
            rowData["parent.id"] = rowData.parent.id;
        }else {/*回显placeholder*/
            $("#parentMenu").each(function (i) {
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if (targetInput) {
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
        $("#menu_dialog").dialog("setTitle", "编辑菜单");
        $("#menu_dialog").dialog("open");
        /*选中的数据回显*/
        $("#menu_form").form("load",rowData);
    });
    /*监听删除按钮的点击*/
    $("#del").click(function () {
        /*获取当前选中的行*/
        var rowData = $("#menu_datagrid").datagrid("getSelected");
        if (!rowData) {
            $.messager.alert("提示", "选择一行数据进行删除");
            return;
        }
        /*提醒用户,是否做删除操作*/
        $.messager.confirm("确认", "是否做删除操作", function (res) {
            if (res) {
                /*做离职操作*/
                $.get("/deleteMenu?id=" + rowData.id, function (data) {
                    /*get请求返回的是json数据  不需要解析*/
                    if (data.success) {
                        $.messager.alert("温馨提示", data.msg);
                        /*重新加载数据表格*/
                        $("#menu_datagrid").datagrid("reload");
                        $("#parentMenu").combobox("reload");
                    } else {
                        $.messager.alert("温馨提示", data.msg);
                    }
                });
            }
        });

    });
    /*监听保存按钮*/
    $("#save").click(function () {
        /*判断当前是添加 还是编辑 编辑操作带有id*/
        var id = $("[name='id']").val();
        var url;
        if (id) {
            var parent_id = $("[name='parent.id']").val();
            if (id==parent_id){
                $.messager.alert("温馨提示", "不能设置自己为父菜单");
                return;
            }
            /*编辑*/
            url = "updateMenu";
        } else {
            /*添加*/
            url = "saveMenu";
        }
        /*提交表单*/
        $("#menu_form").form("submit", {
            url: url,
            success: function (data) {
                data = $.parseJSON(data);
                if (data.success) {
                    $.messager.alert("温馨提示", data.msg);
                    /*关闭对话框*/
                    $("#menu_dialog").dialog("close");
                    /*父菜单重新加载*/
                    $("#parentMenu").combobox("reload");
                    /*重新加载数据*/
                    $("#menu_datagrid").datagrid("reload");
                } else {
                    $.messager.alert("温馨提示", data.msg);
                }
            }
        });
    });
    /*父菜单 下拉列表*/
    $("#parentMenu").combobox({
        width: 150,
        panelHeight: 'auto',
        editable: false,
        url: "/parentList",
        textField: 'text',
        valueField: 'id',
        onLoadSuccess: function () { /*数据加载完毕之后回调*/
            $("#parentMenu").each(function (i) {
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if (targetInput) {
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
    });
});
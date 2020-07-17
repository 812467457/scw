<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<link rel="stylesheet" href="../ztree/zTreeStyle.css">
<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <%-- zTree生成动态节点所依附的静态节点 --%>
                    <ul id="treeDemo" class="ztree">

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%@include file="include-js.jsp" %>
<%@include file="modal-menu-add.jsp" %>
<%@include file="modal-menu-confirm.jsp" %>
<%@include file="modal-menu-edit.jsp" %>
<script src="../ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="../scw/my-menu.js"></script>
<script>
    $(function () {
        //初始化树形结构
        generateTree();

        <%--****************************************添加********************************************--%>
        //给节点按添加钮绑定单击函数
        $("#treeDemo").on("click", ".addBtn", function () {
            //获取当前节点id（当前节点父节点id）
            window.pid = this.id;
            //打开模态框
            $("#menuAddModal").modal("show");
            return false;
        });

        //给保存按钮绑定单击函数
        $("#menuSaveBtn").click(function () {
            //收集用户输入的数据
            var name = $.trim($("#menuAddModal [name=name]").val());
            var url = $.trim($("#menuAddModal [name=url]").val());
            var icon = $("#menuAddModal [name=icon]:checked").val();
            $.ajax({
                "url": "menu/save.json",
                "type": "post",
                "data": {
                    "pid": window.pid,
                    "name": name,
                    "url": url,
                    "icon": icon
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功");
                        //刷新树形结构
                        generateTree();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            //关闭模态框
            $("#menuAddModal").modal("hide");

            //清理模态框,自动点击重置按钮
            $("#menuResetBtn").click();
        });

        <%--****************************************修改********************************************--%>
        //给节点按修改钮绑定单击函数
        $("#treeDemo").on("click", ".editBtn", function () {
            //获取当前节点id
            window.id = this.id;
            //打开模态框
            $("#menuEditModal").modal("show");
            //获取zTreeObj对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            //根据id查询节点对象
            var key = "id";
            var value = window.id;
            var currentNode = zTreeObj.getNodeByParam(key, value);
            //回显数据
            $("#menuEditModal [name=name]").val(currentNode.name);
            $("#menuEditModal [name=url]").val(currentNode.url);
            $("#menuEditModal [name=icon]").val([currentNode.icon]);

            return false;
        });
        //给更新按钮绑定单击函数
        $("#menuEditBtn").click(function () {
            //收集表单数据
            let name = $("#menuEditModal [name=name]").val();
            let url = $("#menuEditModal [name=url]").val();
            let icon = $("#menuEditModal [name=icon]:checked").val();
            //发送Ajax请求
            $.ajax({
                "url": "menu/update.json",
                "type": "post",
                "data": {
                    "id": window.id,
                    "name": name,
                    "url": url,
                    "icon": icon
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功");
                        //刷新树形结构
                        generateTree();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            //关闭模态框
            $("#menuEditModal").modal("hide");
        });

        <%--****************************************删除********************************************--%>
        $("#treeDemo").on("click", ".removeBtn", function () {
            //打开模态框
            $("#menuConfirmModal").modal("show");
            //获取当前节点id
            window.id = this.id;
            return false;
        });
        //给确认删除按钮绑定单击函数
        $("#confirmBtn").click(function () {
            //发送Ajax请求
            $.ajax({
                "url": "menu/remove.json",
                "type": "post",
                "data": {
                    "id": window.id,
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功");
                        //刷新树形结构
                        generateTree();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            //关闭模态框
            $("#menuConfirmModal").modal("hide");
        });
    });
</script>
</html>


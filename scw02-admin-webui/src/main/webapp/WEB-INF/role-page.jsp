<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<link rel="stylesheet" href="../ztree/zTreeStyle.css">

<link rel="stylesheet" href="../css/pagination.css">
<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" class="btn btn-danger" id="batchRemoveBtn"
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%@include file="include-js.jsp" %>
<%@include file="modal-role-add.jsp" %>
<%@include file="modal-role-edit.jsp" %>
<%@include file="modal-role-confirm.jsp" %>
<script src="../ztree/jquery.ztree.all-3.5.min.js"></script>
<%@include file="modal-role-assign-auth.jsp" %>
<script src="../jquery/jquery.pagination.js"></script>
<script src="../scw/my-role.js"></script>
<script>

    <%--****************************************分页********************************************--%>
    $(function () {
        //初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";

        //调用执行分页的函数
        generatePage();

        //条件查询
        //给查询按钮绑定单击函数
        $("#searchBtn").click(function () {
            //获取关键词
            window.keyword = $("#keywordInput").val();
            window.pageNum = 1;
            //调用分页函数刷新页面
            generatePage();
        });

        <%--****************************************新增********************************************--%>
        //点击新增按钮弹出模态框
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });

        //给模态框的保存按钮绑定单击函数
        $("#saveRoleBtn").click(function () {
            //获取文本框数据
            var roleName = $.trim($("#inputRoleName").val());
            //发Ajax请求
            $.ajax({
                "url": "role/save.json",
                "type": "post",
                "data": {
                    "name": roleName
                },
                "datatype": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功!");
                        //重新加载分页
                        window.pageNum = 9999999;
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText)
                }
            });
            //关闭模态框
            $("#addModal").modal("hide");

            //清理模态框
            $("#inputRoleName").val(" ");
        });

        <%--****************************************更新********************************************--%>
        //给修改按钮绑定单击函数，因为按钮是动态生成的，所以使用jQuery对象的in()函数获取按钮对象
        $("#rolePageBody").on("click", ".pencilBtn", function () {
            //打开模态框
            $("#editModal").modal("show");
            //回显数据,获取当前行的角色名称
            var roleName = $(this).parent().prev().text();
            $("#editModal [name=roleName]").val(roleName);
            //获取roleId
            window.roleId = this.id;
        });

        //更新模态框中的更新按钮添加单击事件
        $("#updateRoleBtn").click(function () {
            var roleName = $("#editModal [name=roleName]").val();
            $.ajax({
                "url": "role/update.json",
                "type": "post",
                "data": {
                    "id": window.roleId,
                    "name": roleName
                },
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功!");

                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText)
                }

            });
            //关闭模态框
            $("#editModal").modal("hide");
        });

        <%--****************************************删除********************************************--%>
        // var roleArray = [{roleId:5,roleName:"abc"},{roleId:6,roleName:"ccc"},{roleId:6,roleName:"bbb"}];
        // showConfirmModal(roleArray);
        //确认删除按钮
        $("#removeRoleBtn").click(function () {
            //从全局遍历获取数组roleIdArray，转为json字符串
            var requestBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                "url": "role/remove/by/role/id/array.json",
                "type": "post",
                "data": requestBody,
                "contentType": "application/json;charset=UTF-8",
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功!");

                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText)
                }
            });
            //关闭模态框
            $("#confirmModal").modal("hide");
        });

        //单条删除按钮
        //给修改按钮绑定单击函数，因为按钮是动态生成的，所以使用jQuery对象的in()函数获取按钮对象
        $("#rolePageBody").on("click", ".removeBtn", function () {
            //获取roleName
            var roleName = $(this).parent().prev().text();

            //创建roleArray存入数组
            var roleArray = [{
                roleId: this.id,
                roleName: roleName
            }];

            showConfirmModal(roleArray);
        });

        //批量删除
        //给checkBox绑定单击函数
        $("#summaryBox").click(function () {
            //获取当前多选框状态
            var currentStatus = this.checked;
            //使用当前多选框设置其他多选框
            $(".itemBox").prop("checked", currentStatus);
        });
        //小checkBox全选或全不选也对应到大checkBox
        $("#rolePageBody").on("click", ".itemBox", function () {
            //获取当前已经选中的小box的数量
            var checkedBoxCount = $(".itemBox:checked").length;
            //获取全部小box数量
            var totalBoxCount = $(".itemBox").length;
            //大box的选中状态根据小box的选中的数量和小box的数量来决定,相等就设置选中
            $("#summaryBox").prop("checked", checkedBoxCount === totalBoxCount);
        });
        //给批量删除按钮绑定单击事件
        $("#batchRemoveBtn").click(function () {
            //创建数组对象存放角色对象
            var roleArray = [];

            //遍历获取每个被选中的box
            $(".itemBox:checked").each(function () {
                //获取roleId
                var roleId = this.id;
                //通过DOM操作获取roleName
                var roleName = $(this).parent().next().text();
                roleArray.push({
                    "roleId": roleId,
                    "roleName": roleName
                });
            });
            //检查roleArray是否为空
            if (roleArray.length === 0) {
                layer.msg("请选择要删除的角色！");
                return;
            }
            //调用打开模态框的函数
            showConfirmModal(roleArray);
        });
        <%--****************************************权限********************************************--%>
        //给修改权限按钮绑定单击函数
        $("#rolePageBody").on("click", ".checkBtn", function () {
            //把当前角色id存入全局变量
            window.roleId = this.id;
            //打开模态框
            $("#assignModal").modal("show");
            //模态框中加载树形结构
            fillAuthTree();
        });

        //给权限模态框的修改按钮绑定单击函数
        $("#assignBtn").click(function () {
            //获取被勾选节点
            var authIdArray = [];
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            var checkedNodes = zTreeObj.getCheckedNodes();
            for (var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }
            //发送请求执行分配
            var requestBody = {
                "authIdArray": authIdArray,
                "roleId": [window.roleId]
            };

            requestBody = JSON.stringify(requestBody);

            $.ajax({
                "url": "assign/do/role/assign/auth.json",
                "type": "post",
                "data": requestBody,
                "contentType": "application/json;charset=UTF-8",
                "dataType": "json",
                "success": function (response) {
                    let result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功");
                    }
                    if (result === "FAILED"){
                        layer.msg("操作失败");
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            $("#assignModal").modal("hide");
        });
    });
</script>


</html>


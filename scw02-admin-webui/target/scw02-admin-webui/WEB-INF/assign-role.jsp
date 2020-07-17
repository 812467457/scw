<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form method="post" action="assign/do/role/assign.html" role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <%--@declare id="exampleinputpassword1"--%><label
                                for="exampleInputPassword1">未分配角色列表</label><br>
                            <select class="form-control" multiple="multiple" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                    style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="multiple" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.roleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button id="submitBtn" style="width: 150px;margin-left: 100px;" class="btn btn-lg btn-success btn-block" type="submit"> 提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="include-js.jsp" %>
</body>
<script>
    $(function () {
        $("#toRightBtn").click(function () {
            //找到select标签选择的元素 select:eq(0)>option:selected选择第一个select被选中的子元素 appendTo()把选中的元素添加到指定的地方
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");
        });
        $("#toLeftBtn").click(function () {
            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        });

        //给提交按钮绑定一个单击函数，把所有已分配的角色都选中，否则默认提交的只有选中的部分
        $("#submitBtn").click(function () {
            $("select:eq(1)>option").prop("selected","selected");
        });
    });
</script>
</html>


//生成分页效果
function generatePage() {
    //获取分页数据
    var pageInfo = getPageInfoRemote();

    //填充表格
    fillTableBody(pageInfo);


}

//远程获取pageInfo
function getPageInfoRemote() {
    //调用Ajax函数发送请求并接受Ajax函数的返回值
    var ajaxResult = $.ajax({
        "url": "/role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        "dataType": "json"
    });
    console.log(ajaxResult);

    //判断请求是否成功
    var statusCode = ajaxResult.status;

    //请求处理失败，停止程序
    if (statusCode !== 200) {
        layer.msg("服务器调用失败！");
        return null;
    }

    //处理成功
    var resultEntity = ajaxResult.responseJSON;
    var result = resultEntity.result;

    //判断result是否成功
    if (result === "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }

    //确认result成功后获取pageInfo
    var pageInfo = resultEntity.data;

    //返回pageInfo
    return pageInfo;


}

//填充表格
function fillTableBody(pageInfo) {
    //清除tbody旧数据，方便后面的数据追加
    $("#rolePageBody").empty();
    $("#Pagination").empty();

    //判断pageInfo是否有效
    if (pageInfo === null || pageInfo === undefined || pageInfo.list === null || pageInfo.list.length === 0) {
        $("#rolePageBody").append("<tr><td colspan='4'>抱歉！没又查询到你搜索的数据。</td></tr>>");
        return null;
    }

    //使用pageInfo的list属性填充tbody
    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;

        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkBoxTd = "<td><input class='itemBox' id='" + roleId + "' type='checkbox'></td>";
        var roleNameTd = "<td>" + roleName + "</td>";
        var checkBtn = "<button type='button' id='" + roleId + "' class='btn btn-success btn-xs checkBtn'><i class='glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button type='button' id='" + roleId + "' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button type='button' id='" + roleId + "' class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>"
        var tr = "<tr>" + numberTd + checkBoxTd + roleNameTd + buttonTd + "</tr>"
        $("#rolePageBody").append(tr);
    }
    generateNavigator(pageInfo);
}

//生成页码导航栏
function generateNavigator(pageInfo) {
    //获取总记录数
    var totalRecord = pageInfo.total;
    //声明相关属性
    //声明pagination要设置的属性
    var properties = {
        num_edge_entries: 3, //边缘页数
        num_display_entries: 5, //主体页数
        callback: paginationCallBack,   //点击翻页时的回调函数
        items_per_page: pageInfo.pageSize, //每页显示1项
        current_page: pageInfo.pageNum - 1,   //当前页面，Pagination内部的pageNum从0开始
        prev_text: "上一页",
        next_text: "下一页"
    };
    //初始化pagination
    $("#Pagination").pagination(totalRecord, properties);
}

//执行翻页
function paginationCallBack(pageIndex, jQuery) {
    window.pageNum = pageIndex + 1;
    //调用分页函数
    generatePage();

    //取消页码超链接
    return false;
}

//声明一个打开确认删除模态框的函数
function showConfirmModal(roleArray) {
    //打开模态框
    $("#confirmModal").modal("show");
    //清除模态框旧数据
    $("#roleNameDiv").empty();

    window.roleIdArray = [];

    //获取角色Id和名称
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");

        var roleId = role.roleId;
        window.roleIdArray.push(roleId);
    }

}

//显示auth模态框的树形结构
function fillAuthTree() {
    //发送Ajax查询auth的数据
    var ajaxReturn = $.ajax({
        "url": "assign/get/all/auth.json",
        "type": "post",
        "dataType": "json",
        "async": false,
    });
    if (ajaxReturn.status !== 200) {
        layer.msg("操作失败: " + ajaxReturn.status);
        return;
    }


    //从服务器获取Auth的Json数据
    var authList = ajaxReturn.responseJSON.data;
    var setting = {
        data: {
            simpleData: {
                //简单json
                enable: true,
                //使用categoryId关联父id
                pIdKey: "categoryId"
            },
            key: {
                //使用title属性作为节点名称
                name: "title"
            }
        },
        check: {
            enable: true
        }
    };
    //生成树形结构
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
    //获取zTreeObj对象
    let zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    //展开所有节点
    zTreeObj.expandAll(true);
    //查询已分配的Auth的id数组
    ajaxReturn = $.ajax({
        "url": "assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "data": {
            "roleId": window.roleId
        },
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status !== 200) {
        layer.msg("操作失败: " + ajaxReturn.status);
        return;
    }
    //从响应结果获取authIdList
    var authIdArray = ajaxReturn.responseJSON.data;
    //把已分配的勾选
    for (var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        zTreeObj.checkNode(treeNode, true, false);
    }
}
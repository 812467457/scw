//生成树形结构
function generateTree() {
//从Ajax请求得到json数据
    $.ajax({
        "url": "menu/get/whole/tree.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            var result = response.result;
            if (result === "SUCCESS") {
                //从响应中获取用来生成树形结构的json数据
                var zNodes = response.data;
                //使用json对象存储zTree所做的设置
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom": myAddHoverDom,
                        "removeHoverDom": myRemoveHoverDom
                    },
                    "data": {
                        "key": {
                            //点击节点不跳转
                            "url": "notExist"
                        }
                    }
                };
                //初始化zTree
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if (result === "FAILED") {
                layer.msg(response.message);
            }
        },
        "error": function (response) {
        }
    });
}

/**
 * 生成图标
 * @param treeId    整个树形结构附属的ul标签的id
 * @param treeNode  当前节点的全部数据
 */
function myAddDiyDom(treeId, treeNode) {
    //根据id生成规则拼接出来span标签的id
    var spanId = treeNode.tId + "_ico";
    //删除旧的class 添加新的class
    $("#" + spanId).removeClass().addClass(treeNode.icon);
}

//鼠标移入节点范围添加按钮组
function myAddHoverDom(treeId, treeNode) {
    //为span设置一个id，方便移除的时候找到元素
    var btnGroupId = treeNode.tId + "_btnGrp";

    //判断是否存在按钮组
    if ($("#" + btnGroupId).length > 0) {
        return;
    }

    //各个按钮的HTML标签
    var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改权限信息'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    //获取当前节点级别
    var level = treeNode.level;

    //拼接字符串用的变量
    var btnHTML = "";

    //判断当前节点
    //根节点，只有添加功能
    if (level === 0) {
        btnHTML = addBtn
    }
    //分支节点，修改、删除和添加
    if (level === 1) {
        btnHTML = addBtn + " " + editBtn;
        //没有当前节点的子节点就可以删除该节点
        var length = treeNode.children.length;
        if (length === 0) {
            btnHTML = btnHTML + removeBtn;
        }
    }

    //叶子节点，修改和删除功能
    if (level === 2) {
        btnHTML = editBtn + " " + removeBtn;
    }

    //按钮组附着在超链接后面，先获取超链接的标签
    var anchorId = treeNode.tId + "_a";

    //附加按钮组
    $("#" + anchorId).after("<span id='" + btnGroupId + "'> " + btnHTML + " </span>");

}

//鼠标移出节点范围删除按钮组
function myRemoveHoverDom(treeId, treeNode) {
    var btnGroupId = treeNode.tId + "_btnGrp";

    //移除对应元素
    $("#" + btnGroupId).remove();
}



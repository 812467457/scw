<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade" tabindex="-1" role="dialog" id="confirmModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">删除角色</h4>
            </div>
            <div class="modal-body" style="text-align: center">
                <h4>请确认是否要删除以下角色：</h4>
                <div id="roleNameDiv"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" id="removeRoleBtn" class="btn btn-primary">确认删除</button>
            </div>
        </div>
    </div>
</div>


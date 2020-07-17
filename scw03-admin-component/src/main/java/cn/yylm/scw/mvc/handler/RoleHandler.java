package cn.yylm.scw.mvc.handler;

import cn.yylm.scw.entity.Role;
import cn.yylm.scw.service.api.RoleService;
import cn.yylm.scw.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleHandler {
    @Autowired
    RoleService roleService;

    @PreAuthorize("hasRole('部长')")  //加角色权限
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
        return ResultEntity.successWithData(pageInfo);
    }

    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role) {
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role) {
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeRoleByIdArray(@RequestBody List<Integer> roleIdList) {
        roleService.removeRole(roleIdList);
        return ResultEntity.successWithoutData();
    }


}

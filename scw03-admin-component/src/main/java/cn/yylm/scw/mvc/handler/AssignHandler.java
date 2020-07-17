package cn.yylm.scw.mvc.handler;

import cn.yylm.scw.entity.Auth;
import cn.yylm.scw.entity.Role;
import cn.yylm.scw.service.api.AdminService;
import cn.yylm.scw.service.api.AuthService;
import cn.yylm.scw.service.api.RoleService;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;

    @RequestMapping("/assign/do/role/assign/auth.json")
    @ResponseBody
    public ResultEntity<String> saveRoleAuthRelationship(@RequestBody Map<String,List<Integer>> map){
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRole(@RequestParam("roleId") Integer roleId) {
        List<Integer> authIdList = authService.getAssignedAuthIdByRole(roleId);
        return ResultEntity.successWithData(authIdList);
    }

    /**
     * 查询所有权限
     *
     * @return
     */
    @RequestMapping("/assign/get/all/auth.json")
    @ResponseBody
    public ResultEntity<List<Auth>> getAllAuth() {
        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }

    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(@RequestParam(value = "adminId") Integer adminId,
                                            @RequestParam(value = "pageNum") Integer pageNum,
                                            @RequestParam(value = "keyword") String keyword,
                                            //允许不分配任何角色
                                            @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList) {
        adminService.saveAdminRoleRelationship(adminId, roleIdList);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 跳转到给用户分配角色的页面
     *
     * @param adminId
     * @param model
     * @return
     */
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(@RequestParam(value = "adminId") Integer adminId, Model model) {
        //查询已分配的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        //查询未分配的角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);
        //存入模型
        model.addAttribute("roleList", assignedRoleList);
        model.addAttribute("unAssignedRoleList", unAssignedRoleList);

        return "assign-role";
    }
}

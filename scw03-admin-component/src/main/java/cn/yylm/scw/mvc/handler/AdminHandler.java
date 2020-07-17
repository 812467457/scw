package cn.yylm.scw.mvc.handler;

import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.Admin;
import cn.yylm.scw.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {
    @Autowired
    AdminService adminService;

    /**
     * 更新用户
     * @param admin
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/update.html")
    public String updateAdmin(Admin admin,
                              @RequestParam("pageNum") Integer pageNum,
                              @RequestParam("keyword") Integer keyword){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum;

    }

    /**
     * 回显并修改用户信息
     * @param adminId
     * @param model
     * @return
     */
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId, Model model){
        //根据id查询admin对象
        Admin admin = adminService.getAdminById(adminId);
        //把admin存入model
        model.addAttribute("admin",admin);
        return "admin-edit";
    }

    /**
     * 添加用户
     * @param admin
     * @return
     */
    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum=" + Integer.MAX_VALUE;
    }

    /**
     * 删除
     * @param adminId
     * @param pageNum
     * @param keyword
     * @return
     */
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String removeAdmin(@PathVariable Integer adminId,
                              @PathVariable Integer pageNum,
                              @PathVariable String keyword) {
        adminService.remove(adminId);
        //重定向
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    /**
     * 分页查询：
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            //使用RequestParam注解的defaultValue属性指定默认值为空，不分页查询，若有参数就使用分页查询
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            //默认取第一页
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            //默认每页显示5挑数据
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            Model model) {
        //调用service
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        //把pageInfo存入model
        model.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

//    @RequestMapping("/admin/do/logout.html")
//    public String logout(HttpSession session) {
//        //强制session失效
//        session.invalidate();
//        return "redirect:/admin/to/login/page.html";
//    }

//    @RequestMapping("/admin/do/login.html")
//    public String doLogin(@RequestParam("loginAcct") String loginAcct,
//                          @RequestParam("userPswd") String userPswd,
//                          HttpSession session) {
//        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
//
//        //把登录成功后的admin对象存入session
//        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
//        //避免重复提交表单，使用重定向到页面
//        return "redirect:/admin/to/main/page.html";
//    }
}

package cn.yylm.scw.mvc.handler;

import cn.yylm.scw.entity.Admin;
import cn.yylm.scw.service.api.AdminService;
import cn.yylm.scw.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {
    Logger log = LoggerFactory.getLogger(TestHandler.class);

    @Autowired
    private AdminService adminService;

    @RequestMapping("/test/ssm")
    public String testSsm(Model model, HttpServletRequest request) {
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);
        log.info("testSsm{}", judgeRequestType);
        List<Admin> admins = adminService.getAll();
        model.addAttribute("admins", admins);
        //int i = 1 / 0;
        return "success";
    }


    @ResponseBody
    @RequestMapping("/send/array")
    public String testAjax(@RequestBody List<Integer> array, HttpServletRequest request) {
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);
        log.info("testAjax{}", judgeRequestType);
        for (Integer num : array) {
            log.info("array=" + num);
        }

        return "success";
    }
}

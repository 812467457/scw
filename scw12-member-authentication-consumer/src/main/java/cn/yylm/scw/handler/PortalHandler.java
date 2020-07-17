package cn.yylm.scw.handler;

import cn.yylm.scw.api.MySQLRemoteService;
import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.vo.PortalTypeVO;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class PortalHandler {
    @Resource
    MySQLRemoteService mySQLRemoteService;

    /**
     * 显示首页
     *
     * @return
     */
    @RequestMapping("/")
    public String showPortalPage(Model model) {
        //显示首页数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();
        //检查查询结果
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            //获取查询数据
            List<PortalTypeVO> list = resultEntity.getData();
            //存入model
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATE, list);
        }
        return "portal";
    }


}

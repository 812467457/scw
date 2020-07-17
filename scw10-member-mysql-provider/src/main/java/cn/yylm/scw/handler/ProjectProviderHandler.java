package cn.yylm.scw.handler;

import cn.yylm.scw.entity.vo.DetailProject;
import cn.yylm.scw.entity.vo.PortalTypeVO;
import cn.yylm.scw.entity.vo.ProjectVO;
import cn.yylm.scw.service.api.ProjectService;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ProjectProviderHandler {
    @Resource
    private ProjectService projectService;


    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVo, @RequestParam("memberId") Integer memberId) {
        try {
            projectService.saveProject(projectVo, memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/portal/type/project/data/Remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote() {
        try {
            List<PortalTypeVO> portalTypeVOList = projectService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProject> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId) {
        try {
            DetailProject detailProjectVO = projectService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


}

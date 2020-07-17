package cn.yylm.scw.service.api;

import cn.yylm.scw.entity.vo.DetailProject;
import cn.yylm.scw.entity.vo.PortalTypeVO;
import cn.yylm.scw.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVo, Integer memberId);

    List<PortalTypeVO> getPortalTypeVO();

    DetailProject getDetailProjectVO(Integer projectId);
}

package cn.yylm.scw.mapper;


import java.util.List;

import cn.yylm.scw.entity.po.ProjectPO;
import cn.yylm.scw.entity.po.ProjectPOExample;
import cn.yylm.scw.entity.vo.DetailProject;
import cn.yylm.scw.entity.vo.DetailReturnVO;
import cn.yylm.scw.entity.vo.PortalProjectVO;
import cn.yylm.scw.entity.vo.PortalTypeVO;
import org.apache.ibatis.annotations.Param;

public interface ProjectPOMapper {
    int countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void insertTypeRelationship(@Param("typeIdList") List<Integer> typeIdList, @Param("projectId") Integer projectId);

    void insertTagRelationship(@Param("tagIdList") List<Integer> tagIdList, @Param("projectId") Integer projectId);

    List<PortalTypeVO> selectPortalTypeVOList();

    PortalProjectVO selectPortalProjectVOList();

    DetailProject selectDetailProjectVO(Integer projectId);

    String selectDetailPicturePah();

    DetailReturnVO selectDetailReturnVO();
}
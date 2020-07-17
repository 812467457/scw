package cn.yylm.scw.service.impl;

import cn.yylm.scw.entity.po.MemberConfirmInfoPO;
import cn.yylm.scw.entity.po.MemberLaunchInfoPO;
import cn.yylm.scw.entity.po.ProjectPO;
import cn.yylm.scw.entity.po.ReturnPO;
import cn.yylm.scw.entity.vo.*;
import cn.yylm.scw.mapper.*;
import cn.yylm.scw.service.api.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {
    @Resource
    private ProjectPOMapper projectPOMapper;

    @Resource
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Resource
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Resource
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Resource
    private ReturnPOMapper returnPOMapper;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVo, Integer memberId) {
        //保存projectPO
        //创建projectPo对象
        ProjectPO projectPO = new ProjectPO();
        //把VO属性复制到PO
        BeanUtils.copyProperties(projectVo, projectPO);
        projectPO.setMemberid(memberId);
        String createdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createdate);
        projectPO.setStatus(0);
        //保存projectPO
        projectPOMapper.insertSelective(projectPO);
        //从projectPo获取自增长主键
        Integer projectId = projectPO.getId();

        //保存项目分类、分类的关联信息
        //获取typeList
        List<Integer> typeIdList = projectVo.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        //保存项目标签关联信息
        //获取typeTagIdList
        List<Integer> tagIdList = projectVo.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList, projectId);

        //保存项目详情图片信息
        List<String> detailPicturePathList = projectVo.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);

        //保存项目发起人信息
        MemberLaunchInfoVO memberLaunchInfoVO = projectVo.getMemberLaunchInfoVO();
        //把VO对象转换为Po对象
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLaunchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        //保存项目回报信息
        List<ReturnVO> returnVOList = projectVo.getReturnVOList();
        //把VO对象转换为Po对象
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOList.add(returnPO);
        }
        returnPOMapper.insertReturnPOBoth(returnPOList, projectId);


        //保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVo.getMemberConfirmInfoVO();
        //把VO对象转换为Po对象
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProject getDetailProjectVO(Integer projectId) {
        //给状态码赋值
        DetailProject detailProject = projectPOMapper.selectDetailProjectVO(projectId);
        Integer status = detailProject.getStatus();
        switch (status) {
            case 0:
                detailProject.setStatusTest("审核中");
                break;
            case 1:
                detailProject.setStatusTest("众筹中");
                break;
            case 2:
                detailProject.setStatusTest("众筹成功");
                break;
            case 3:
                detailProject.setStatusTest("众筹关闭");
                break;
            default:
                break;
        }
        //根据deployDate计算lastDay
        String deployDate = detailProject.getDeployDate();
        //获取当前日期
        Date currentDay = new Date();
        //把众筹日期转为Date类型
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = simpleDateFormat.parse(deployDate);
            //获取时间戳
            long currentTime = currentDay.getTime();
            long deployTime = deployDay.getTime();
            //计算众筹还剩多少天
            long pastDays = (currentTime - deployTime) / 1000 / 60 / 60 / 24;
            Integer totalDays = detailProject.getDay();
            Integer lastDay = (int) (totalDays - pastDays);
            detailProject.setLastDay(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return detailProject;
    }
}

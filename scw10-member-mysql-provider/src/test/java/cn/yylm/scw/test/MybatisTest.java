package cn.yylm.scw.test;

import cn.yylm.scw.entity.po.Member;
import cn.yylm.scw.entity.vo.DetailProject;
import cn.yylm.scw.entity.vo.PortalProjectVO;
import cn.yylm.scw.entity.vo.PortalTypeVO;
import cn.yylm.scw.mapper.MemberMapper;
import cn.yylm.scw.mapper.ProjectPOMapper;
import cn.yylm.scw.service.api.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {
    @Autowired
    private DataSource dataSource;
    @Resource
    private MemberMapper memberMapper;

    @Resource
    private ProjectPOMapper projectPOMapper;

    @Resource
    private MemberService memberService;

    Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Test
    public void Test() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.debug("connection = " + connection);
    }

    @Test
    public void Test2() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123456";
        String encode = passwordEncoder.encode(source);
        Member member = new Member(null, "jack112312", encode, "jack123", "jack@163.com", 0, 1, "杰克", "45464665", 0);
        memberMapper.insert(member);

    }

    @Test
    public void test03() {
        Member jack = memberService.getMemberByLoginAcct("jack");
        System.out.println(jack);
    }

    @Test
    public void test04() {
        List<PortalTypeVO> portalTypeVOS = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO portalTypeVO : portalTypeVOS) {
            String name = portalTypeVO.getName();
            String remark = portalTypeVO.getRemark();
            logger.debug("name:" + name + "------ remark:" + remark);
            List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
            for (PortalProjectVO portalProjectVO : portalProjectVOList) {
                if (portalProjectVO == null) {
                    continue;
                }
                logger.info(portalProjectVO.toString());
            }
        }
    }

    @Test
    public void test05(){
        DetailProject detailProject = projectPOMapper.selectDetailProjectVO(35);
//        logger.debug(detailProject.getProjectId() + "");
//        logger.debug(detailProject.getProjectName() + "");
//        logger.debug(detailProject.getProjectDesc() + "");
//        logger.debug(detailProject.getFollowerCount() + "");
//        logger.debug(detailProject.getStatus() + "");
//        logger.debug(detailProject.getMoney() + "");
//        logger.debug(detailProject.getSupportMoney() + "");
//        logger.debug(detailProject.getPercentage() + "");
//        logger.debug(detailProject.getDeployDate() + "");
//        logger.debug(detailProject.getSupporterCount() + "");
//        logger.debug(detailProject.getHeaderPicturePath()+ "");

        logger.debug(detailProject.toString());

    }
}

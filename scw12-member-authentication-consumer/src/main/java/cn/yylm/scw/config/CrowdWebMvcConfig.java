package cn.yylm.scw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器访问的地址
        String regUrlPath = "/auth/member/to/reg/page";
        //目标视图的名称
        String regViewName = "member-reg";
        registry.addViewController(regUrlPath).setViewName(regViewName);

        String loginUrlPath = "/auth/member/to/login/page";
        String loginViewName = "member-login";
        registry.addViewController(loginUrlPath).setViewName(loginViewName);

        String memberCenterUrlPath = "/auth/member/to/center/page";
        String memberCenterViewName = "member-center";
        registry.addViewController(memberCenterUrlPath).setViewName(memberCenterViewName);

        String memberCrowdUrlPath = "/member/my/crowd";
        String memberCrowdViewName = "member-crowd";
        registry.addViewController(memberCrowdUrlPath).setViewName(memberCrowdViewName);

        String projectStartPath = "/project/agree/protocol/page";
        String projectStartViewName = "project-start";
        registry.addViewController(projectStartPath).setViewName(projectStartViewName);
    }
}

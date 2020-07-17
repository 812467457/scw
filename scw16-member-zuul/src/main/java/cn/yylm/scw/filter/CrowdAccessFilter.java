package cn.yylm.scw.filter;


import cn.yylm.scw.constant.AccessPassResources;
import cn.yylm.scw.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public boolean shouldFilter() {
        //获取requestContext
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取当前请求对象
        HttpServletRequest request = requestContext.getRequest();
        //获取servletPath
        String servletPath = request.getServletPath();
        //判断是不是特定资源
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);
        //如果是true就直接放行
        if (containsResult) {
            return false;
        }
        //判断是不是静态资源
        boolean staticResult = AccessPassResources.judgeCurrentServletPathWhetherStaticResource(servletPath);
        //如果不是静态资源就不放行，来到run()方法
        return !staticResult;


    }

    @Override
    public Object run() throws ZuulException {
        //获取requestContext
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取当前请求对象
        HttpServletRequest request = requestContext.getRequest();
        //获取当前session对象
        HttpSession session = request.getSession();
        //从session中获取登录用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        //判断loginMember
        if (loginMember == null) {
            //未登录跳转登录页面并给出提示消息
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
            //重定向到登录页面,向获取response对象
            HttpServletResponse response = requestContext.getResponse();
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public String filterType() {
        //在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}

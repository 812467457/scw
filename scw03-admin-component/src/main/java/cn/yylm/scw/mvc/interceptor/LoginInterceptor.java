package cn.yylm.scw.mvc.interceptor;

import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.Admin;
import cn.yylm.scw.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 检查登录的拦截器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //通过request对象获取session
        HttpSession session = request.getSession();

        //从session域获取admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);

        //判断admin对象是否为空
        if (admin == null) {
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
        }

        //admin对象不为空，放行
        return true;
    }
}

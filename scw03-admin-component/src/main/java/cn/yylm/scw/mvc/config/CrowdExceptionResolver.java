package cn.yylm.scw.mvc.config;

import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.exception.AccessForbiddenException;
import cn.yylm.scw.exception.LoginAcctAlreadyInUseException;
import cn.yylm.scw.exception.LoginAcctAlreadyInUseForUpdateException;
import cn.yylm.scw.exception.LoginFailedException;
import cn.yylm.scw.util.CrowdUtil;
import cn.yylm.scw.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@ControllerAdvice   //表示当前类为基于注解异常处理类
public class CrowdExceptionResolver {
    /**
     * 处理异常的通用方法
     *
     * @param e
     * @param request
     * @param response
     * @param viewName
     * @return
     * @throws IOException
     */
    private ModelAndView commonResolve(Exception e, HttpServletRequest request, HttpServletResponse response, String viewName) throws IOException {
        //判断当前请求类型
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);
        //Ajax请求
        if (judgeRequestType) {
            //创建resultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(e.getMessage());
            //创建gson对象
            Gson gson = new Gson();
            //把resultEntity转化为json字符串
            String json = gson.toJson(resultEntity);
            //把json返回
            response.getWriter().write(json);
            return null;
        }

        //不是Ajax请求
        //创建modelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        //将exception对象存入modelAndView
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, e);
        //设置视图
        modelAndView.setViewName(viewName);
        //返回modelAndView
        return modelAndView;
    }

    /**
     * 处理NullPointerException
     */
    @ExceptionHandler(value = NullPointerException.class)   //把一个具体的异常和一个方法关联起来
    public ModelAndView resolveNullPointerException(NullPointerException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(e, request, response, viewName);
    }

    /**
     * 处理登录异常
     *
     * @param e
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginFailedException.class)   //把一个具体的异常和一个方法关联起来
    public ModelAndView resolveLoginException(LoginFailedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(e, request, response, viewName);
    }
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveLoginException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(e, request, response, viewName);
    }

    /**
     * 处理未登录访问受保护资源的异常
     *
     * @param e
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(AccessForbiddenException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(e, request, response, viewName);
    }

    /**
     * 处理账号重复异常
     *
     * @param e
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolve(e, request, response, viewName);
    }
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(LoginAcctAlreadyInUseException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(e, request, response, viewName);
    }
}

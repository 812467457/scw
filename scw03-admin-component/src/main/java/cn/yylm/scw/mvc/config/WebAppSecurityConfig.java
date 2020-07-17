package cn.yylm.scw.mvc.config;

import cn.yylm.scw.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableGlobalMethodSecurity(prePostEnabled = true)      //启用全局方法权限控制功能，保证有关权限的注解生效
@Configuration      //表示当前类是配置类
@EnableWebSecurity  //在web环境下启用SpringSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //使用内存版验证代码
        //builder.inMemoryAuthentication().withUser("jack").password("123456").roles("ADMIN");
        //基于数据库认证
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()                                         //对请求授权
                .antMatchers("/admin/to/login/page.html")    //放行登录页
                .permitAll()                                               //无条件访问
                .antMatchers("/bootstrap/**")               //放行静态资源
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/layer/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/scw/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()
                .antMatchers("/admin/get/page.html")
                .access("hasRole('经理') OR hasAuthority('user:get')")                                 //针对Admin显示分页进行角色权限控制
                .anyRequest()                                   //其他任意请求
                .authenticated()                                //认证后访问
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception",new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .csrf()
                .disable()                                      //禁用csrf
                .formLogin()                                    //开启表单登录配置
                .loginPage("/admin/to/login/page.html")         //登录页面
                .loginProcessingUrl("/security/do/login.html")  //处理登录请求地址
                .usernameParameter("loginAcct")                 //账号的请求参数名称
                .passwordParameter("userPswd")                  //密码的请求参数名称
                .defaultSuccessUrl("/admin/to/main/page.html")  //登录成功后的页面
                .and()
                .logout()                                       //开启退出功能
                .logoutUrl("/security/do/logout.html")          //指定退出的地址
                .logoutSuccessUrl("/admin/to/login/page.html")  //退出成功后跳转的页面

        ;
    }
}

package cn.yylm.scw.mvc.handler;

import cn.yylm.scw.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AuthHandler {
    @Autowired
    AuthService authService;


}

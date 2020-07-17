package cn.yylm.scw.mvc.config;

import cn.yylm.scw.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * 使用原始的Admin对象对User类扩展
 */
public class SecurityAdmin extends User {
    private Admin originalAdmin;    //原始Admin对象

    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}

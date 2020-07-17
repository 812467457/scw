package cn.yylm.scw.test;

import cn.yylm.scw.entity.Admin;
import cn.yylm.scw.entity.Role;
import cn.yylm.scw.mapper.AdminMapper;
import cn.yylm.scw.mapper.RoleMapper;
import cn.yylm.scw.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class) //整合spring环境的JUnit
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class ScwTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void test() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();
    }

    @Test
    public void test2() {
        Admin admin = new Admin(null, "marry", "456", "马瑞", "marry@163.com", null);
        int insert = adminMapper.insert(admin);
        System.out.println(insert);
    }
    @Test
    public void test3() {
        Admin admin = adminMapper.selectByPrimaryKey(1);
        System.out.println(admin);
    }
    @Test
    public void test4() {
        Admin admin = new Admin(null,"zhangsan1234","123","张三","adawd@163.com",null);
        adminService.saveAdmin(admin);
    }
    @Test
    public void Test05(){
        for (int i = 0; i < 55; i++) {
            roleMapper.insert(new Role(null,"role" + i));
        }
    }

}

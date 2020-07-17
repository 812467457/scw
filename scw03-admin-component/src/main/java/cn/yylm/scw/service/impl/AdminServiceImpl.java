package cn.yylm.scw.service.impl;

import cn.yylm.scw.constant.CrowdConstant;
import cn.yylm.scw.entity.Admin;
import cn.yylm.scw.entity.AdminExample;
import cn.yylm.scw.exception.LoginAcctAlreadyInUseException;
import cn.yylm.scw.exception.LoginAcctAlreadyInUseForUpdateException;
import cn.yylm.scw.exception.LoginFailedException;
import cn.yylm.scw.mapper.AdminMapper;
import cn.yylm.scw.service.api.AdminService;
import cn.yylm.scw.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveAdmin(Admin admin) {
        //密码加密
        String userPswd = admin.getUserPswd();
        //userPswd = CrowdUtil.md5(userPswd);
        userPswd = passwordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);

        //生成创建时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(date);
        admin.setCreateTime(createTime);
        //保存
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        //根据账号查询admin对象
        AdminExample adminExample = new AdminExample(); //创建AdminExample对象
        AdminExample.Criteria criteria = adminExample.createCriteria(); //创建criteria对象
        criteria.andLoginAcctEqualTo(loginAcct);    //封装查询条件
        List<Admin> admins = adminMapper.selectByExample(adminExample);//执行查询语句
        //判断admin对象是否为null 为null抛异常
        if (admins == null || admins.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (admins.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_UNIQUE);
        }

        Admin admin = admins.get(0);
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        //不为null把admin对象的密码从数据库取出，把名文密码加密后比较
        String userPswdDB = admin.getUserPswd();
        String userPswdForm = CrowdUtil.md5(userPswd);
        if (!Objects.equals(userPswdForm, userPswdDB)) {
            //密码不一致抛异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //密码一致返回admin
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        //调用pageHelper的方法开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        //执行查询
        List<Admin> admins = adminMapper.selectByKeyword(keyword);
        for (Admin admin : admins) {

        }
        //封装到pageInfo
        return new PageInfo<>(admins);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        //有选择的更新，null值字段不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        //把旧的数据删除再把新的数据保存
        adminMapper.deleteRelationship(adminId);
        //保存新的关联
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String userName) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(userName);
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        return admins.get(0);
    }


}

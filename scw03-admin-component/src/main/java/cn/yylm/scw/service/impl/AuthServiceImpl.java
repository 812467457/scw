package cn.yylm.scw.service.impl;

import cn.yylm.scw.entity.Auth;
import cn.yylm.scw.entity.AuthExample;
import cn.yylm.scw.mapper.AuthMapper;
import cn.yylm.scw.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRole(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRole(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        //获取roleId
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        //删除旧关联
        authMapper.deleteOldRelationship(roleId);
        //获取authList
        List<Integer> authIdList = map.get("authIdArray");
        //判断authIdList是否有效
        if (authIdList != null && authIdList.size() > 0) {
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAuthNameByAdminId(adminId);
    }
}

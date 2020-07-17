package cn.yylm.scw.service.api;

import cn.yylm.scw.entity.Auth;

import java.util.List;
import java.util.Map;

public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAssignedAuthIdByRole(Integer roleId);

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}

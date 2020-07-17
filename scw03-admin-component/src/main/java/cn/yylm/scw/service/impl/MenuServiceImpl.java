package cn.yylm.scw.service.impl;

import cn.yylm.scw.entity.Menu;
import cn.yylm.scw.entity.MenuExample;
import cn.yylm.scw.mapper.MenuMapper;
import cn.yylm.scw.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void deleteMenu(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }
}

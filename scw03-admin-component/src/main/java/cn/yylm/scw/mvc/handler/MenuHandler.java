package cn.yylm.scw.mvc.handler;

import cn.yylm.scw.entity.Menu;
import cn.yylm.scw.service.api.MenuService;
import cn.yylm.scw.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuHandler {
    @Autowired
    private MenuService menuService;

    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> deleteMenu(Integer id){
        menuService.deleteMenu(id);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTree() {
        //查询所有Menu对象
        List<Menu> menuList = menuService.getAll();
        //存放根节点的变量
        Menu root = null;
        //使用map存储Menu的pid和Menu对象的对应父子关系
        Map<Integer, Menu> menuMap = new HashMap<>();
        //遍历menuList填充mao
        for (Menu menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }

        //再次遍历menuList查找根节点
        for (Menu menu : menuList) {
            //获取当前menu对象的pid
            Integer pid = menu.getPid();
            //如果pid为null就为根节点
            if (pid == null) {
                root = menu;
                continue;
            }
            //pid不为null，说明有父节点
            Menu father = menuMap.get(pid);
            //将当前节点存入父节点的children
            father.getChildren().add(menu);
        }
        //把根节点返回，所有节点都已经包含在内
        return ResultEntity.successWithData(root);
    }
}

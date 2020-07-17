package cn.yylm.scw.constant;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<>();
    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
        PASS_RES_SET.add("http://localhost/project/get/project/detail/");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();
    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    /**
     * 判断当前servletPath是否为静态资源
     * @param servletPath
     * @return
     */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath){
        //排除字符串无效
        if (servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        //拆分servletPath 获取一级路径
        String[] split = servletPath.split("/");
        //  /auth/member/send/short/message.json  切分完后可能是 [" ","auth"......]所以取索引1上的元素
        String firstLeavePath = split[1];
        //判断这个路径是否在静态资源的集合里
        return STATIC_RES_SET.contains(firstLeavePath);
    }

}

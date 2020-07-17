package cn.yylm.scw.test;

import cn.yylm.scw.util.CrowdUtil;
import org.junit.Test;

public class StringTest {
    @Test
    public void test(){
        String source = "123123";
        String s = CrowdUtil.md5(source);
        System.out.println(s);
    }
}

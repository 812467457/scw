package cn.yylm.scw.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("D:/chrom/wall/p1.jpg");
        ResultEntity<String> stringResultEntity = CrowdUtil.uploadFileToOss("http://oss-cn-beijing.aliyuncs.com",
                "LTAI4GCA5zF3nNbvb46ZLPDP",
                "pvYUQcmVuzrgz4yv0ZJJcBjktaq3M1",
                inputStream,
                "scwdemo",
                "http://scwdemo.oss-cn-beijing.aliyuncs.com",
                "7d478c3641bffb262983ad01fc160ab2.jpg"
        );
        System.out.println(stringResultEntity);
    }
}

package cn.yylm.scw;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.yylm.scw.mapper")
@SpringBootApplication
public class MysqlProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MysqlProviderApplication.class, args);
    }
}

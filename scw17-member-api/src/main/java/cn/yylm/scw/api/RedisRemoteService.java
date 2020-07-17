package cn.yylm.scw.api;

import cn.yylm.scw.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@FeignClient("SCW-REDIS-PROVIDER")
public interface RedisRemoteService {
    /**
     * 设置redis的值
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key,
                                                @RequestParam("value") String value);

    /**
     * 设置redis的值并且带过期时间
     * @param key
     * @param value
     * @param time  过期时间
     * @param timeUnit  时间单位
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeOut(@RequestParam("key") String key,
                                                           @RequestParam("value") String value,
                                                           @RequestParam("time") Long time,
                                                           @RequestParam("timeUnit") TimeUnit timeUnit);

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/value/by/key")
    ResultEntity<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key);

    /**
     * 删除key
     * @param key
     * @return
     */
    @RequestMapping("remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
}

package com.itheima.job;

import com.itheima.utils.QiniuUtils;
import redis.clients.jedis.JedisPool;

import java.util.Set;

import static com.itheima.pojo.RedisConstant.SETMEAL_PIC_DB_RESOURCES;
import static com.itheima.pojo.RedisConstant.SETMEAL_PIC_RESOURCES;

public class ClearImgJob {
    private JedisPool jedisPool;
    public void deletRedisImg() {
        //传入两个redis存的不同容器得到差集
        Set<String> sdiff = jedisPool.getResource().sdiff(SETMEAL_PIC_RESOURCES, SETMEAL_PIC_DB_RESOURCES);
        //遍历差集,得到文件名调用方法删除
        if (sdiff != null) {
            for (String s : sdiff) {
                QiniuUtils.deleteFileFromQiniu(s);
                jedisPool.getResource().srem(SETMEAL_PIC_RESOURCES, s);
            }
        }
    }

}

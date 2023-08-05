package com.github.desperado2.data.open.api;


import com.github.desperado2.data.open.api.common.manage.config.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(value = Runner.class)
class OpenDataPlatformApplicationTests {


    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test1() {
        Long aLong = redisUtil.increase("test-213123");
        System.out.println(aLong);
        long l = redisUtil.addValue("test-213123", 0);
        System.out.println(l);
    }
}

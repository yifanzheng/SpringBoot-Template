package top.yifan.template.config;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.yifan.template.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DevConfigurationTest {

    @Value("${env}")
    private String env;

    @Test
    public void testConfig() {
        Assert.assertNotNull(env);
    }
}
package com.shj;

import com.shj.entity.LHS;
import com.shj.service.DroolsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by xiaoyaolan on 2017/3/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DecisionApplicationTests {

    @Autowired
    DroolsService droolsService;
    @Test
    public void contextLoads() {
        LHS lhs = new LHS();
        lhs.put("test", "天天");
        System.out.println(droolsService.invokeAudit(lhs));

        try {
            System.out.println(droolsService.invokeAuditTest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

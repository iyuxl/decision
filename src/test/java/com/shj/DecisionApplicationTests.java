package com.shj;

import com.shj.entity.XFact;
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
        XFact XFact = new XFact();
        XFact.put("test", "天天");
        XFact.put("x", "2");
        System.out.println(droolsService.invokeAudit(XFact));

        try {
           // System.out.println(droolsService.invokeAuditTest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

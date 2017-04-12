package com.shj.service.task;

import com.google.common.collect.Lists;
import com.shj.entity.LHS;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by xiaoyaolan on 2017/4/12.
 */
public class DroolsFiresTask implements Callable{
    private LHS lhs;
    private KieBase kieBase;
    public DroolsFiresTask(KieBase kieBase, LHS lhs) {
        this.kieBase = kieBase;
        this.lhs = lhs;
    }
    @Override
    public Object call() throws Exception {
        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(lhs);
        kieSession.fireAllRules();
        Iterator it = kieSession.getObjects().iterator();
        List<?> rs = Lists.newArrayList(it);
        kieSession.dispose();
        return rs;
    }
}

package com.shj.service.task;

import com.google.common.collect.Lists;
import com.shj.entity.LHS;
import com.shj.service.DroolsService;
import org.drools.core.SessionConfiguration;
import org.drools.core.impl.EnvironmentFactory;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by xiaoyaolan on 2017/4/12.
 */
public class DroolsFiresTask implements Callable{
    private static final Logger LOG = LoggerFactory.getLogger(DroolsFiresTask.class);

    private LHS lhs;
    private KieBase kieBase;
    public DroolsFiresTask(KieBase kieBase, LHS lhs) {
        this.kieBase = kieBase;
        this.lhs = lhs;
    }
    @Override
    public Object call() throws Exception {
        long begin = System.currentTimeMillis();
        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(lhs);
        kieSession.fireAllRules();
        Iterator it = kieSession.getObjects().iterator();
        List<?> rs = Lists.newArrayList(it);
        kieSession.dispose();
        LOG.info("fire time use " + (System.currentTimeMillis() - begin));
        return rs;
    }
}

package com.shj.service.task;

import com.google.common.collect.Lists;
import com.shj.entity.XFact;
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

    private XFact XFact;
    private KieBase kieBase;
    public DroolsFiresTask(KieBase kieBase, XFact XFact) {
        this.kieBase = kieBase;
        this.XFact = XFact;
    }
    @Override
    public Object call() throws Exception {
        long begin = System.currentTimeMillis();
        KieSession kieSession = kieBase.newKieSession();
        try {
            kieSession.insert(XFact);
            kieSession.fireAllRules();
            Iterator it = kieSession.getObjects().iterator();
            List<?> rs = Lists.newArrayList(it);
            return rs;
        } finally {
            kieSession.dispose();
            LOG.info("fire time use " + (System.currentTimeMillis() - begin));
        }
    }
}

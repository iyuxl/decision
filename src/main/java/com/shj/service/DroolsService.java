package com.shj.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.shj.entity.FactResult;
import com.shj.entity.LHS;
import com.shj.service.task.DroolsFiresTask;
import org.apache.commons.io.IOUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by xiaoyaolan on 2017/3/30.
 */
@Component
public class DroolsService {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsService.class);

    @Autowired
    private KieBase kieBase;
    @Autowired
    private KieContainer kieContainer;
    @Autowired
    KieServices ks;

    @Autowired
    ListeningExecutorService listeningExecutorService;
    /**
     * 重載規則腳本文件
     * @param prePath
     * @param files
     * @return
     */
    public boolean reloadRules(String prePath, Resource[] files) {
        final KieRepository kieRepository = ks.getRepository();
        KieFileSystem kfs = ks.newKieFileSystem();
        for (Resource file : files) {
            try {
                String myString = IOUtils.toString(file.getInputStream(), Charsets.UTF_8);
            } catch (IOException e) {
                LOG.error("文件内容获取失败", e);
            }
            kfs.write(ResourceFactory.newClassPathResource(prePath + file.getFilename(), Charsets.UTF_8.name()));
        }
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        if (!kieBuilder.getResults().getMessages().isEmpty()) {
            LOG.warn(JSON.toJSONString(kieBuilder.getResults().getMessages(), true));
            return false;
        }
        kieRepository.addKieModule(kieBuilder.getKieModule());
        Results results = kieContainer.updateToVersion(kieRepository.getDefaultReleaseId());
        if (results.hasMessages(Message.Level.ERROR)) {
            LOG.warn(JSON.toJSONString(kieBuilder.getResults().getMessages(), true));
            return false;
        }
        return true;
    }

    /**
     * 重載規則腳本流
     * @param inputStream
     * @return
     */
    public boolean reloadRules(InputStream inputStream) {
        try {
            final KieRepository kieRepository = ks.getRepository();
            KieFileSystem kfs = ks.newKieFileSystem();
            kfs.write(ResourceFactory.newInputStreamResource(inputStream, Charsets.UTF_8.name()));
            KieBuilder kieBuilder = ks.newKieBuilder(kfs);
            if (!kieBuilder.getResults().getMessages().isEmpty()) {
                LOG.warn(JSON.toJSONString(kieBuilder.getResults().getMessages(), true));
                return false;
            }
            kieRepository.addKieModule(kieBuilder.getKieModule());
            Results results = kieContainer.updateToVersion(kieRepository.getDefaultReleaseId());
            if (results.hasMessages(Message.Level.ERROR)) {
                LOG.warn(JSON.toJSONString(kieBuilder.getResults().getMessages(), true));
                return false;
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return true;
    }

    public List<?> invokeAudit(LHS lhs) {
        DroolsFiresTask dt = new DroolsFiresTask(kieBase, lhs);
        try {
            ListenableFuture<List<?>> lf = listeningExecutorService.submit(dt);
            Futures.addCallback(lf, new FutureCallback<List<?>>() {
                @Override
                public void onSuccess(List<?> result) {
                    if (LOG.isDebugEnabled()) {
                        LOG.info("invoke rule success:" + JSON.toJSONString(result, true));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    LOG.error("invoke rule error:" + Throwables.getStackTraceAsString(t));
                }
            });
            return lf.get();
        } catch (Exception e) {
            LOG.error("invoke rule error:" + Throwables.getStackTraceAsString(e.fillInStackTrace()));
        }
        return Collections.emptyList();
    }

    public List<?> invokeAudit(List<LHS> lists) {
        KieSession kieSession = kieBase.newKieSession();
        for (LHS object : lists) {
            kieSession.insert(object);
        }
        kieSession.fireAllRules();
        Iterator it = kieSession.getObjects().iterator();
        List<?> rs = Lists.newArrayList(it);
        while (it.hasNext()) {
            LOG.info(JSON.toJSONString(it.next()));
        }
        kieSession.dispose();
        return rs;
    }

    private Resource[] listRules() throws Exception {
        PathMatchingResourcePatternResolver pmrs = new PathMatchingResourcePatternResolver();
        Resource[] resources = pmrs.getResources("classpath*:rulestest/*.drl");
        return resources;
    }

    public String invokeAuditTest() throws Exception {
        Resource[] files = listRules();
        reloadRules("rulestest/", files);
        KieSession kieSession = kieBase.newKieSession();
        LHS lhs = new LHS();
        kieSession.insert(lhs);
        kieSession.fireAllRules();
        Iterator it = kieSession.getObjects().iterator();
        kieSession.dispose();
        return JSON.toJSONString(it);
    }
}

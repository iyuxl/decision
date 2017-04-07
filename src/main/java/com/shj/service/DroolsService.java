package com.shj.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Iterator;

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


    public String invokeAudit() {
        KieSession kieSession = kieBase.newKieSession();
        List<String> myGlobalList = Lists.newArrayList();
        kieSession.setGlobal("myGlobalList", myGlobalList);
        kieSession.fireAllRules();
        Iterator it = kieSession.getObjects().iterator();
        while (it.hasNext()) {
            LOG.info(JSON.toJSONString(it.next()));
        }
        kieSession.dispose();
        return myGlobalList.get(0);
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
        List<String> myGlobalList = Lists.newArrayList();
        kieSession.setGlobal("myGlobalList", myGlobalList);
        kieSession.fireAllRules();
        Iterator it = kieSession.getObjects().iterator();
        while (it.hasNext()) {
            LOG.info(JSON.toJSONString(it.next()));
        }
        kieSession.dispose();
        return myGlobalList.get(0);
    }
}

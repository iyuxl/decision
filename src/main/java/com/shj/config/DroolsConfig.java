package com.shj.config;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.drools.core.audit.WorkingMemoryInMemoryLogger;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * Created by xiaoyaolan on 2017/3/27.
 */
@Configuration
public class DroolsConfig {

    @Bean
    public KieServices ks() {
        return KieServices.Factory.get();
    }

    @Autowired KieServices ks;
    @Bean
    @ConditionalOnMissingBean(KieContainer.class)
    public KieContainer kieContainer() throws IOException {
        final KieRepository kieRepository = ks.getRepository();
        kieRepository.addKieModule(new KieModule() {
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
        Resource[] files = listRules();
        KieFileSystem kfs = ks.newKieFileSystem();
        for(Resource file : files) {
            String myString = IOUtils.toString(file.getInputStream(), Charsets.UTF_8);
            kfs.write(ResourceFactory.newClassPathResource("rules/" + file.getFilename(), Charsets.UTF_8.name()));
        }

        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        kieBuilder.buildAll();
        Results results = kieBuilder.getResults();
        if( results.hasMessages( Message.Level.ERROR ) ){
            throw new IllegalStateException(JSON.toJSONString(results.getMessages(), true));
        }
        return ks.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    private Resource[] listRules() throws IOException {
        PathMatchingResourcePatternResolver pmrs = new PathMatchingResourcePatternResolver();
        Resource[] resources = pmrs.getResources("classpath*:rules/*.drl");
        return resources;
    }

    @Autowired
    KieContainer kieContainer;

    @Bean
    public KieBase kieBase() throws IOException {
        KieBase kieBase = kieContainer.getKieBase();
        kieBase.addEventListener(new WorkingMemoryInMemoryLogger());
        return kieBase;
    }
}

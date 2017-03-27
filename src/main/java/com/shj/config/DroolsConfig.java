package com.shj.config;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
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
    public KieContainer kieContainer() throws IOException {
        KieServices ks = KieServices.Factory.get();
        final KieRepository kr = ks.getRepository();
        kr.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kr.getDefaultReleaseId();
            }
        });
        KieFileSystem kfs = ks.newKieFileSystem();
        Resource[] files = listRules();

        for(Resource file : files) {
            String myString = IOUtils.toString(file.getInputStream(), Charsets.UTF_8);
            //kfs.write("src/main/resources/"+ file.getFilename(), myString);
            kfs.write(ResourceFactory.newClassPathResource(file.getFilename(), Charsets.UTF_8.name()));
        }

        KieBuilder kb = ks.newKieBuilder(kfs);
        kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
        KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
        return kContainer;
    }

    private Resource[] listRules() throws IOException {
        PathMatchingResourcePatternResolver pmrs = new PathMatchingResourcePatternResolver();
        Resource[] resources = pmrs.getResources("classpath*:*.drl");
        return resources;
    }

    @Bean
    public KieBase kieBase() throws IOException {
        KieBase kieBase = kieContainer().getKieBase();
        return kieBase;
    }
}

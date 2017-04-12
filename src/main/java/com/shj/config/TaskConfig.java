package com.shj.config;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by xiaoyaolan on 2017/4/12.
 */
@Configuration
public class TaskConfig {

    @Value("${drools.task.size:5}")
    private int droolsTaskSize;

    /**
     * 定義線程池，用於規則執行使用
     * @return
     */
    @Bean
    public ListeningExecutorService listeningExecutorService() {
        return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(droolsTaskSize, new ThreadFactoryBuilder().setNameFormat("invoke-drools-%d").build()));
    }
}

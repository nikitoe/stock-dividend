package com.nikitoe.stockdividend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();

        // 코어의 갯수 가져오기
        int coreNumber = Runtime.getRuntime().availableProcessors();

        // thread의 갯수 설정하기
        threadPool.setPoolSize(coreNumber);
        threadPool.initialize();

        // 설정한 thread값 저장
        taskRegistrar.setTaskScheduler(threadPool);
    }
}

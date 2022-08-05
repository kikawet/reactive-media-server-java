package com.kikawet.reactiveMediaServer.beans;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class SchedulerBean {	
	@Bean
    Scheduler jdbcScheduler(@Value("${spring.task.scheduling.pool.size}") int maxThreads) {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(maxThreads));
    }
}

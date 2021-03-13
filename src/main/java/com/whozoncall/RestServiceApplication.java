package com.whozoncall;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootApplication
@EnableAsync
public class RestServiceApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class, args);   
    }
    
    @Bean
    public Executor taskExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(3);
      executor.setMaxPoolSize(3);
      executor.setQueueCapacity(20);
      executor.setThreadNamePrefix("Whozoncall-");
      executor.initialize();
      return executor;
    }
    
}

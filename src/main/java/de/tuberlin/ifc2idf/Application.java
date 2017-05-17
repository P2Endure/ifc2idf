package de.tuberlin.ifc2idf;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Spring Boot Main Application
 */
//TODO enable caching. to use guava for local cache or ElastiCache for distributed cache solution.

@SpringBootApplication
@EnableAutoConfiguration
@Profile("localTest")
@EnableAsync
public class Application extends AsyncConfigurerSupport{


    public static void main( String[] args ) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("ifc2idf-");
        executor.initialize();
        return executor;
    }
}

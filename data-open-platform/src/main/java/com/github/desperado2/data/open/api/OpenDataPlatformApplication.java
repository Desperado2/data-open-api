package com.github.desperado2.data.open.api;

import com.github.desperado2.data.open.api.validator.CustomPropertyValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = { CacheAutoConfiguration.class})
@EnableAsync
@EnableTransactionManagement
public class OpenDataPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenDataPlatformApplication.class, args);
    }

    @Bean
    public static  CustomPropertyValidator configurationPropertiesValidator(){
        return new CustomPropertyValidator();
    }
}

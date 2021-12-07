package com.blog.photoapp.api.gateway.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

@Configuration
public class GlobalFiltersConfiguration {
    private final Logger logger = LoggerFactory.getLogger(PreFilter.class);

    //pre filter executed first (lower idx = higher priority)
    //post filter executed last (lower idx = lower priorty) 
    @Order(1)
    @Bean
    public GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            logger.info("second pre filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("second Post filter executed");
            }));
        };
    }

    @Order(2)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            logger.info("third pre filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("first   Post filter executed");
            }));
        };
    }
}

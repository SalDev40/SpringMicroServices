package com.blog.photoapp.api.gateway.ApiGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

//runs after every http request route is routed to endpoint and endpoint runs
@Component
public class PostFilter implements GlobalFilter, Ordered {
    private final Logger logger = LoggerFactory.getLogger(PreFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("last Post filter executed");
        }));
    }

    //this filter will be executed last in chain
    @Override
    public int getOrder() {
        return 0;
    }
}

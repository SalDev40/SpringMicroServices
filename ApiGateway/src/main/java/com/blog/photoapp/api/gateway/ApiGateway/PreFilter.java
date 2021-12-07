package com.blog.photoapp.api.gateway.ApiGateway;

import java.util.Set;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

//globalpre  filter will run for every http request route 
//before it is routed to endpoint
@Component
public class PreFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(PreFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("first pre filter");

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        HttpHeaders headers = request.getHeaders();
        Set<String> headerNames = headers.keySet();

        logger.info("=> Request Path: " + path);
        logger.info("=> Headers");

        headerNames.forEach((headerName) -> {
            String headerValue = headers.getFirst(headerName);
            logger.info(headerName + " : " + headerValue);

        });

        return chain.filter(exchange);
    }

    //this filter will be executed first in chain
    @Override
    public int getOrder() {
        return 0;
    }

}

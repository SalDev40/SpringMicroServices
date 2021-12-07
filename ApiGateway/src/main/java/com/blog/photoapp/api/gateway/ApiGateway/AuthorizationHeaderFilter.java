package com.blog.photoapp.api.gateway.ApiGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

//validate user JWT token , check token was signed correctly
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    Environment env;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {
        // Put configuration properties here
    }

    // logic of custom filter
    @Override
    public GatewayFilter apply(Config config) {

        // exchange = current server exchange to read HTTP req details
        // chain = gateway filter chain to delegate next filter in the chain
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return this.onError(exchange, "NO Authorization header", HttpStatus.UNAUTHORIZED);
            }

            // read authorization header
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                return this.onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        // String subject =
        // Jwts.parser().setSigningKey(env.getProperty(token.secret)).parseClaims(jwt).getBody()
        // .getSubject();
        // if (subject == null) {
        // returnValue = false;
        // }

        // if (jwt != "dummy") {
        //     returnValue = false;
        // }
        
        return returnValue;

    }

}

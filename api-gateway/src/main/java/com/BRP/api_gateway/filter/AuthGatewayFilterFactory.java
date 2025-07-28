package com.BRP.api_gateway.filter;
import com.BRP.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return this.onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);

                try {
                    Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                    Claims claims = jwtUtil.extractAllClaims(token);

                    String username = claims.get("username", String.class);
                    Long userId = claims.get("userId", Long.class);
                    String role = claims.get("role", String.class);

                    // Mutate request with user info headers
                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-Username", username)
                            .header("X-User-Id", userId.toString())
                            .header("X-User-Role", role)
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());

                } catch (Exception e) {
                    System.out.println("Exception occured due to : "+e);
                    return this.onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                System.out.println("Exception occured due to : "+e);
                return this.onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // You can later add config fields if needed
    }
}
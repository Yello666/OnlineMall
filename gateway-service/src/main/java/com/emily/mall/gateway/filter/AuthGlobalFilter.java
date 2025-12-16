package com.emily.mall.gateway.filter;

import com.emily.mall.common.jwtUtils.jwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 网关全局认证与权限过滤器
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final jwtUtils jwtUtils;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获得请求
        ServerHttpRequest request = exchange.getRequest();
        //请求路由
        String path = request.getURI().getPath();
        //请求方法
        String method = request.getMethodValue();

        //请求头Authorization字段
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (StringUtils.hasText(authHeader)) {
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            } else {
                token = authHeader;
            }
        }
        //2.解析JWT
        Claims claims = null;
        if (StringUtils.hasText(token)) {
            try {
                claims = jwtUtils.parseToken(token);
            } catch (Exception e) {
                log.warn("解析JWT失败, path={}, error={}", path, e.getMessage());
                return writeJson(exchange.getResponse(), HttpStatus.UNAUTHORIZED, 401, "Token无效或登陆已过期");
            }
        }
        //3.登陆校验
        boolean loginRequired = !isPublicPath(path, method);

        // 需要登录但没有有效token
        if (loginRequired && claims == null) {
            return writeJson(exchange.getResponse(), HttpStatus.UNAUTHORIZED, 401, "未登录或登录已过期");
        }

        String role = null;
        String userId = null;
        String username = null;
        if (claims != null) {
            role = (String) claims.get("role");
            username = (String) claims.get("username");
            userId = claims.getSubject();
        }

        // 4.权限校验
        if (!hasAccess(path, method, role)) {
            return writeJson(exchange.getResponse(), HttpStatus.FORBIDDEN, 403, "权限不足");
        }

        // 4.将解析出的用户信息透传到下游服务
        if (claims != null) {
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId == null ? "" : userId)
                    .header("X-User-Name", username == null ? "" : username)
                    .header("X-User-Role", role == null ? "" : role)
                    .build();
            exchange = exchange.mutate().request(mutatedRequest).build();
        }

        return chain.filter(exchange);
    }

    /**
     * 是否是无需登录即可访问的路径
     */
    private boolean isPublicPath(String path, String method) {
        // 登录、注册
        if (pathMatcher.match("/user/login", path) || pathMatcher.match("/user/register", path)) {
            return true;
        }

        // 未登录也可以查看某个用户的信息: GET /user/{id}
        if ("GET".equalsIgnoreCase(method) && pathMatcher.match("/user/*", path)) {
            return true;
        }

        // 未登录也可以查看/查询商品: 所有 GET /product/**
        if ("GET".equalsIgnoreCase(method) && pathMatcher.match("/product/**", path)) {
            return true;
        }

        return false;
    }

    /**
     * 是否有访问权限
     */
    private boolean hasAccess(String path, String method, String role) {
        // 访问商品增删改接口时，只有商户和管理员允许
        if (isProductModify(path, method)) {
            if ("ROLE_SALER".equals(role) || "ROLE_ADMIN".equals(role)) {
                return true;
            }
            // 其他登录用户无权操作商品增删改
            return false;
        }

        // 其他接口：只要通过了登录校验（有token或公共接口）即可访问
        return true;
    }

    /**
     * 是否是商品的增删改操作
     */
    private boolean isProductModify(String path, String method) {
        if (!pathMatcher.match("/product/**", path)) {
            return false;
        }
        String m = method.toUpperCase();
        return "POST".equals(m) || "PUT".equals(m) || "DELETE".equals(m);
    }

    private Mono<Void> writeJson(ServerHttpResponse response, HttpStatus httpStatus, int code, String message) {
        response.setStatusCode(httpStatus);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        String body = String.format("{\"code\":%d,\"message\":\"%s\",\"data\":null,\"success\":false}", code, message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public int getOrder() {
        // 数值越小优先级越高，这里放在较前位置
        return 0;
    }
}

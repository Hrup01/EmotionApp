package com.groupb.util.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.groupb.util.jwt.JwtTokenProvider;

import java.net.URI;
import java.util.Map;

/**
 * 握手拦截器 - 增强版，支持JWT验证
 */
@Component
@Slf4j
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("{}开始握手", request.getRemoteAddress());

        URI uri = request.getURI();
        String query = uri.getQuery();

        //方案1:从URL参数中获取token
        String token = extractTokenFromQuery(query, "token");

        //方案2:如果方案1没有，尝试从Authorization header获取
        if (token == null) {
            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {//匹配Bearer
                token = authHeader.substring(7);//从第七个索引开始，获取token
            }
        }

        //验证JWT token并获取用户信息
        if (token != null) {
            //验证token是否有效（格式和过期时间）
            if (jwtTokenProvider.validateToken(token)) {
                //从token中获取用户名
                String username = jwtTokenProvider.getUsernameFromToken(token);

                if (username != null) {
                    //进一步验证：验证token和用户名是否匹配
                    if (jwtTokenProvider.validateToken(token, username)) {
                        //获取用户ID
                        Long userId = jwtTokenProvider.getUserIdFromToken(token);

                        log.info("JWT验证成功，用户: {} (ID: {})", username, userId);

                        //将用户信息存储到attributes中，供WebSocketHandler使用
                        attributes.put("username", username);
                        if (userId != null) {
                            attributes.put("userId", userId);
                        }
                        attributes.put("token", token);
                        return super.beforeHandshake(request, response, wsHandler, attributes);
                    } else {
                        log.error("JWT token与用户名不匹配");
                    }
                } else {
                    log.error("无法从JWT token中提取用户名");
                }
            } else {
                log.error("JWT token无效或已过期");
            }
        } else if (query != null && query.contains("username=")) {
            //向后兼容：如果没有token，使用uri解析username的方式（基本pass）
            log.warn("使用不安全的username参数方式，建议使用JWT token");
            String username = extractTokenFromQuery(query, "username");
            if (username != null && !username.isEmpty()) {
                attributes.put("username", username);
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        }

        log.error("未找到有效的认证信息，拒绝WebSocket连接");
        return false; // 拒绝握手
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        log.info("{}完成握手", request.getRemoteAddress());
        super.afterHandshake(request, response, wsHandler, ex);
    }

    /**
     * 从查询字符串中提取参数值--从地址栏解析出来（测试使用多）
     */
    private String extractTokenFromQuery(String query, String paramName) {
        if (query == null || query.isEmpty()) {
            return null;
        }
        String[] params = query.split("&");
        for (String param : params) {
            if (param.startsWith(paramName + "=")) {
                return param.substring(paramName.length() + 1);
            }
        }
        return null;
    }
}


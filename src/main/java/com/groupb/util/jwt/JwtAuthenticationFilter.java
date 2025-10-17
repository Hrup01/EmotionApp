package com.groupb.util.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        String username = null;

        log.debug("处理请求: {} {}", request.getMethod(), request.getRequestURI());
        log.debug("Authorization头: {}", authHeader);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtTokenProvider.getUsernameFromToken(token);
            log.debug("从token中提取用户名: {}", username);
        } else {
            log.warn("请求缺少有效的Authorization头: {}", authHeader);
        }

        if (StringUtils.hasText(username) && jwtTokenProvider.validateToken(token, username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("用户认证成功: {}", username);
        } else {
            log.warn("用户认证失败 - 用户名: {}, token有效: {}", username, jwtTokenProvider.validateToken(token, username));
        }

        filterChain.doFilter(request, response);
    }
}



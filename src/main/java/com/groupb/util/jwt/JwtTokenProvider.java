package com.groupb.util.jwt;

import com.groupb.pojo.User;
import com.groupb.util.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private JwtProperties jwtProperties;

    private SecretKey getSigningKey(){
        byte[] keyBytes = jwtProperties.getSecret().getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成JWT令牌
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT令牌
     */
    public String generateToken(Long userId, String username){
        //基于用户ID和用户名生成令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims, username);
    }

    /**
     * 创建令牌
     * */
    public String createToken(Map<String,Object> claims,String subject){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .setClaims(claims)//添加自定义信息
                .setSubject(subject)
                .setIssuedAt(now)//创建时间
                .setExpiration(expiryDate)//过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)//签名算法
                .compact();//构建令牌
    }

    /**
     * 从令牌中获取数据声明
     * */
    public Claims getClaimsFromToken(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.error("JWT令牌解析错误:{}",e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中获取用户名
     * */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null){
            return claims.getSubject();
        }
        return null;
    }

    /**
     * 从令牌中获取用户ID
     * */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null && claims.get("userId") != null){
            return Long.valueOf(claims.get("userId").toString());
        }
        return null;
    }
    /**
     * 验证令牌
     * @param token JWT令牌
     * @param username 用户名
     * @return 是否有效
     */
    public boolean validateToken(String token, String username){
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null
                    && claims.getSubject().equals(username)
                    && !isTokenExpired(claims);
        }catch (Exception e){
            log.error("JWT令牌验证错误:{}",e.getMessage());
            return false;
        }
    }

    /**
     * 验证令牌（仅验证格式和过期时间）
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token){
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && !isTokenExpired(claims);
        }catch (Exception e){
            log.error("JWT令牌验证错误:{}",e.getMessage());
            return false;
        }
    }

    /**
     * 验证令牌是否过期
     * */
    private boolean isTokenExpired(Claims claims){
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 刷新令牌
     * */
    public String refreshToken(String token){
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null && !isTokenExpired(claims)){
                Long userId = Long.valueOf(claims.get("userId").toString());
                String username = claims.getSubject();
                return generateToken(userId, username);
            }
        } catch (Exception e) {
            log.error("JWT令牌刷新错误:{}",e.getMessage());
        }
        return null;
    }


}

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
     * */
    public String generateToken(Long username, String phone){
        //基于传递的用户名以及电话号生成令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",username);
        claims.put("phone",phone);
        return createToken(claims,phone);

    }

    /**
     * 创建令牌
     * */
    public String createToken(Map<String,Object> claims,String subject){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
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
     * 从令牌中获取手机号
     * */
    public String getPhoneFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null){
            return claims.getSubject();
        }
        return null;
    }
    /**
     * 验证令牌
     * */
    public boolean validateToken(String token,String phone){
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null
                    && claims.getSubject().equals(phone)
                    && !isTokenExpired(claims);
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
                Long username = Long.valueOf(claims.get("username").toString());
                String phone = claims.getSubject();
                return generateToken(username,phone);
            }
        } catch (Exception e) {
            log.error("JWT令牌刷新错误:{}",e.getMessage());
        }
        return null;
    }


}

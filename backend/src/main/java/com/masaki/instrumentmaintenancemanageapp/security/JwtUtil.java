package com.masaki.instrumentmaintenancemanageapp.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 秘密鍵（本番ではapplication.ymlへ移動する）
    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey12345";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // トークン生成
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)              // ユーザー名
                .setIssuedAt(new Date())           // 発行時間
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1時間
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ユーザー名取得
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // トークン検証
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

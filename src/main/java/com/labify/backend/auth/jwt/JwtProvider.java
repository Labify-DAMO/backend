package com.labify.backend.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 토큰 발급
@Service
@Getter
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access-expiration}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpirationTime;

    @Value("${jwt.issuer}")
    private String issuer;

    // refreshToken DB에 토큰 암호화해서 저장하기 위한 메소드
    public String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
    }


    // Access Token 발급
    // subject(sub): 사용자 식별자 (이메일)
    public String generateAccessToken(String subject) {
        long now = System.currentTimeMillis();
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpirationTime))
                .claim("type", "access")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();

        return jwt;
    }

    // Refresh Token 발급
    public String generateRefreshToken(String subject) {
        long now = System.currentTimeMillis();
        String jwt = Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpirationTime))
                .claim("type", "refresh")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();

        return jwt;
    }

    // 토큰 파싱 (Claims(payload 부분) 추출)
    public Claims parseToken(String token) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return payload;

        } catch (ExpiredJwtException e) {
            System.out.println("토큰 만료됨: " + e.getMessage());
            throw e;
        } catch (JwtException e) {
            System.out.println("JWT 관련 기타 오류: " + e.getMessage());
            throw e;
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Date exp = parseToken(token).getExpiration();
            return exp.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}

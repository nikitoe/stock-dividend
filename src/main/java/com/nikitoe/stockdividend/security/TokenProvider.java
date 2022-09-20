package com.nikitoe.stockdividend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;   // 1 hour
    private static final String KEY_ROLES = "roles";


    @Value("${spring.jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성(발급)
     *
     * @param username
     * @param roles
     * @return
     */
    public String generateToken(String username, List<String> roles) {

        // 사용의 권한정보 저장
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        // 토큰 생성된 시간
        var now = new Date();

        // 토큰 만료 시간
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        // 토큰에 주입
        return Jwts.builder()
            .setClaims(claims) // 사용자 권한정보
            .setIssuedAt(now) // 토큰 생성된 시간
            .setExpiration(expiredDate) // 토큰 만료 시간
            .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
            .compact();

    }


    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    /**
     * 토큰 유혀성 검사
     *
     * @param token
     * @return
     */
    public boolean validToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

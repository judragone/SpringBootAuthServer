package com.example.apiserver.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.apiserver.service.CloudDeviceService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Component
public class JwtProvider {
    private String secretKey = "supremasupremasupremasupremasupremasupremasupremasuprema";
    
    private Key key;

    private long tokenValidTime = 60 * 60 * 1000L;     // 1hour

    private final CloudDeviceService cloudDeviceService;

    @PostConstruct
    protected void init() {       
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String pk) {
        Claims claims = Jwts.claims().setSubject(pk); 
        
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) 
                .setIssuedAt(now) 
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Long getUserPk(HttpServletRequest request) {
        return Long.parseLong(Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
        .build().parseClaimsJws(resolveToken(request)).getBody().getSubject());
    }


    public Boolean validateToken(String jwtToken) {
        try {           
            Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
            .build()
            .parseClaimsJws(jwtToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    // Request의 Header에서 token 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring("Bearer ".length());
    }

    public Boolean checkToken(HttpServletRequest request) {
        String token = resolveToken(request);        
            
        if (token != null && validateToken(token)) {            
            return true;            
        }
        return false;
    }
}

package com.cookingBird.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Objects;

public class TokenUtil {

    public static final String TOKEN_HEADER = "Authorization";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final long TTL = 3 * 60 * 60;
    private static final String SECRET = "cookingBird";

    public static String create(Object object) {
        try {
            String string = MAPPER.writeValueAsString(object);
            Date now = new Date();
            Date expiredTime = new Date(now.getTime() + TTL * 1000);
            JwtBuilder builder = Jwts.builder().setHeaderParam("type", "JWT")
                    .setSubject(string)
                    .setIssuedAt(now)
                    .setExpiration(expiredTime)
                    .signWith(SignatureAlgorithm.HS512, SECRET);
            return builder.compact();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T parse(String token, Class<? extends T> clazz) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        if (Objects.isNull(claims)) {
            throw new RuntimeException("unAuthorization");
        }
        String subject = claims.getSubject();
        if (Objects.isNull(subject)) {
            throw new RuntimeException("unAuthorization");
        }
        try {
            return MAPPER.readValue(subject, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean expired(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJwt(token).getBody();
        if (Objects.isNull(claims)) {
            throw new RuntimeException("unAuthorization");
        }
        Date expiration = claims.getExpiration();
        return new Date().before(expiration);
    }

}

package ru.bakhaev.test.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);

    private static final long EXPIRATION_TIME = 36000000;
    private static final String SECRET = "ThisIsASecret";

    public String generateToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return token;
    }

    public boolean checkToken(String token) {
        String email;
        if (!token.equals("")) {
            try {
                // parse the token.
                Jws<Claims> jwsClaims = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token);
                email = jwsClaims.getBody().getSubject();
            } catch (Exception e) {
                LOG.info("Ошибка проверки токена. {}", e);
                return false;
            }
            return (!email.equals(""));
        }
        return false;
    }
}

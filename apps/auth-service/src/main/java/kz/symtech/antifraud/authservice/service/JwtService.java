package kz.symtech.antifraud.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kz.symtech.antifraud.authservice.config.JwtConfig;
import kz.symtech.antifraud.authservice.helpers.JwtKeyProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtConfig jwtConfig;
    private final JwtKeyProvider jwtKeyProvider;

    public String generateToken(String payload) {
        Instant now = Instant.now();

        ZonedDateTime zonedDateTimeNow = ZonedDateTime.ofInstant(now, ZoneId.of("Asia/Almaty"));
        return Jwts.builder()
                .setIssuedAt(Date.from(zonedDateTimeNow.toInstant()))
                .setExpiration(Date.from(zonedDateTimeNow.plusMinutes(jwtConfig.getExpirationInMinutes()).toInstant()))
                .claim("sub", payload)
                .signWith(SignatureAlgorithm.RS256, jwtKeyProvider.getPrivateKey())
                .compact();
    }

    boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(jwtKeyProvider.getPublicKey()).parseClaimsJws(jwt);
            return true;
        } catch(JwtException e) {
            log.error("Invalid JWT!", e);
        }
        return false;
    }

    public String getUsernameFrom(String jwt) {
        return (String) getClaims(jwt).get("sub");
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtKeyProvider.getPublicKey())
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String getJwtFromRequest(HttpServletRequest req, String tokenName) {
        if (req.getCookies() == null) {
            return null;
        }
        List<Cookie> cookies = Arrays.stream(req.getCookies()).filter(cook -> Objects.equals(cook.getName(), tokenName)).collect(Collectors.toUnmodifiableList());
        if (cookies.isEmpty()) {
          return null;
        }
        return cookies.get(0).getValue();
    }
}
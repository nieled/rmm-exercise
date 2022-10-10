package net.nieled.rmmexercise.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import net.nieled.rmmexercise.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class TokenProvider {

    private static final long TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer("nieled")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.trace("JWT expired", ex);
        } catch (IllegalArgumentException ex) {
            log.trace("Token is null, empty or only whitespace", ex);
        } catch (MalformedJwtException ex) {
            log.trace("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            log.trace("JWT is not supported", ex);
        } catch (SignatureException ex) {
            log.trace("Signature validation failed");
        }
        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}

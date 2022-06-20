package ca.utoronto.lms.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {
    @Value("${token.secret}")
    private String secret;

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception exception) {
            return null;
        }
    }

    private boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
        } catch (Exception exception) {
            return true;
        }
    }

    public String getUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (Exception exception) {
            return null;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }
}

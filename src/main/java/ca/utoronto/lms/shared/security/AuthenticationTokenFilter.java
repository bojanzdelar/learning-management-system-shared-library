package ca.utoronto.lms.shared.security;

import ca.utoronto.lms.shared.client.AuthFeignClient;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final AuthFeignClient authFeignClient;
    private final TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authToken = request.getHeader("Authorization");
        String username = tokenUtils.getUsername(authToken);
        if ((username != null && SecurityContextHolder.getContext().getAuthentication() == null)) {
            UserDetails userDetails = getUserDetails(username);
            if (tokenUtils.validateToken(authToken, userDetails)) {
                Claims claims = tokenUtils.getClaims(authToken);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, claims, userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    protected UserDetails getUserDetails(String username) {
        return authFeignClient.getUser(username);
    }
}

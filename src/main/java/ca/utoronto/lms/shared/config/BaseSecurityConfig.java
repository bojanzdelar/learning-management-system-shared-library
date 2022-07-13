package ca.utoronto.lms.shared.config;

import ca.utoronto.lms.shared.feign.AuthFeignClient;
import ca.utoronto.lms.shared.security.AuthenticationTokenFilter;
import ca.utoronto.lms.shared.security.TokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class BaseSecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter(
            AuthFeignClient feignClient, TokenUtils utils, AuthenticationManager manager) {
        AuthenticationTokenFilter tokenFilter = new AuthenticationTokenFilter(feignClient, utils);
        tokenFilter.setAuthenticationManager(manager);
        return tokenFilter;
    }
}

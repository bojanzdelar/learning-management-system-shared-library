package ca.utoronto.lms.shared.client;

import ca.utoronto.lms.shared.dto.UserDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", contextId = "authFeignClient")
public interface AuthFeignClient {
    @GetMapping("/users/username/{username}")
    UserDetailsDTO getUser(@PathVariable String username);
}

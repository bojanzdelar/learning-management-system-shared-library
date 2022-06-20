package ca.utoronto.lms.shared.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long ROLE_ROOT_ID = 1L;
    public static String ROLE_ROOT = "ROLE_ROOT";

    public static Long ROLE_ADMIN_ID = 2L;
    public static String ROLE_ADMIN = "ROLE_ADMIN";

    public static Long ROLE_TEACHER_ID = 3L;
    public static String ROLE_TEACHER = "ROLE_TEACHER";

    public static Long ROLE_STUDENT_ID = 4L;
    public static String ROLE_STUDENT = "ROLE_STUDENT";

    public static boolean hasAuthority(String authority) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(authority));
    }
}

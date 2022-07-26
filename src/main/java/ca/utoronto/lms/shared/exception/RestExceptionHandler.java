package ca.utoronto.lms.shared.exception;

import ca.utoronto.lms.shared.controller.ApiError;
import feign.FeignException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ApiError> handleBadRequest(BadRequestException exception) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ApiError> handleAuthentication(AuthenticationException exception) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<ApiError> handleForbidden(ForbiddenException exception) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler({NotFoundException.class, UsernameNotFoundException.class})
    protected ResponseEntity<ApiError> handleNotFound(Exception exception) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(FeignException.class)
    protected ResponseEntity<ApiError> handleFeignException(FeignException exception) {
        return buildResponseEntity(HttpStatus.valueOf(exception.status()), exception);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleException(Exception exception) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private ResponseEntity<ApiError> buildResponseEntity(HttpStatus status, Exception exception) {
        return new ResponseEntity<>(new ApiError(status, exception.getMessage()), status);
    }
}

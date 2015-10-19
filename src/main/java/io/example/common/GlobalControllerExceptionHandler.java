package io.example.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(HttpServletResponse response, ResourceNotFoundException ex) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), " " + ex.getMessage());
    }

}

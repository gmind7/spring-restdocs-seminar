package io.example.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by KDS on 2015-10-09.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class ResourceNotFoundException extends RuntimeException {

    private static final String _PREFIX_MESSAGE = "Please try again and with a non empty as ";

    public ResourceNotFoundException() {
        super(_PREFIX_MESSAGE+"id");
    }

    public ResourceNotFoundException(String emptyName, Throwable cause) {
        super(_PREFIX_MESSAGE+emptyName, cause);
    }

    public ResourceNotFoundException(String emptyName) {
        super(_PREFIX_MESSAGE+emptyName);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}

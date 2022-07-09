package top.yifan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * RESTfulAPIException
 */
public class RESTfulAPIException extends RuntimeException {

    private static final long serialVersionUID = -4885690136312391780L;
    
    private HttpStatus status;
    
    public RESTfulAPIException(HttpStatus status, @Nullable String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}

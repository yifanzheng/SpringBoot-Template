package top.yifan.exception;


import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * AuthenticationException
 */
public class UnAuthenticationException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 734400356620233612L;

    public UnAuthenticationException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.UNAUTHORIZED);
    }
}

package top.yifan.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * AlreadyExistsException
 */
public class AlreadyExistsException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -8591752451421294755L;

    public AlreadyExistsException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.CONFLICT);
    }

}
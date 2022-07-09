package top.yifan.template.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * NotFoundException
 *
 * @author star
 */
public class NotFoundException extends AbstractThrowableProblem {

    public NotFoundException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.NOT_FOUND);
    }
}

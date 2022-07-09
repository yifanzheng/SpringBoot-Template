package top.yifan.exception;


import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * ParameterErrorException
 */
public class ParameterErrorException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -5707225592017258068L;

    public ParameterErrorException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.BAD_REQUEST);
    }
}

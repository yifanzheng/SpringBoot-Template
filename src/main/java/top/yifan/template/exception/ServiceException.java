package top.yifan.template.exception;

/**
 * ServiceException
 *
 * @author Star.Y.Zheng
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -8773139510494001347L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
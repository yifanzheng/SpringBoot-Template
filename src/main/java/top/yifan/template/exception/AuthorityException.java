package top.yifan.template.exception;

/**
 * AuthorityException
 *
 * @author Star.Y.Zheng
 */
public class AuthorityException extends BadRequestAlertException {

    public AuthorityException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }
}

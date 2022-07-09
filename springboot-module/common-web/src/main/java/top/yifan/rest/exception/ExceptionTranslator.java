package top.yifan.rest.exception;


import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.validation.ConstraintViolationProblem;
import top.yifan.exception.BadRequestAlertException;
import top.yifan.exception.ParameterErrorException;
import top.yifan.exception.RESTfulAPIException;
import top.yifan.rest.util.HeaderUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

    /**
     * Post-process Problem payload to add the message key for front-end if needed
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null || entity.getBody() == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        ProblemBuilder builder = Problem.builder()
                .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
                .withStatus(problem.getStatus())
                .withDetail(problem.getTitle())
                .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI())
                .with("timestamp", new Date());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                    .with("violations", ((ConstraintViolationProblem) problem).getViolations())
                    .with("message", ErrorConstants.ERR_VALIDATION);
        } else if (problem instanceof DefaultProblem) {
            builder
                    .withCause(((DefaultProblem) problem).getCause())
                    .withDetail(problem.getDetail())
                    .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
                builder.with("message", "error.http." + problem.getStatus().getStatusCode());
            }
        }

        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode()))
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withType(ErrorConstants.DEFAULT_TYPE)
                .withTitle("Method argument not valid")
                .withStatus(defaultConstraintViolationStatus())
                .with("message", ErrorConstants.ERR_VALIDATION)
                .with("fieldErrors", fieldErrors)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .with("message", ErrorConstants.DEFAULT_TYPE)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(ParameterErrorException.class)
    public ResponseEntity<Problem> handleParamaterErrorException(ParameterErrorException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withType(ErrorConstants.DEFAULT_TYPE)
                .withStatus(Status.BAD_REQUEST)
                .with("message", ErrorConstants.ERR_VALIDATION)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(BadRequestAlertException.class)
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withType(ErrorConstants.DEFAULT_TYPE)
                .withStatus(Status.CONFLICT)
                .with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(RESTfulAPIException.class)
    public ResponseEntity<Problem> handleRESTfulAPIException(RESTfulAPIException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.valueOf(ex.getStatus().name()))
                .withDetail(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Problem> handleServiceException(ServiceException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withDetail(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<?> restTemplateException(
            NativeWebRequest request, HttpStatusCodeException ex) {
        // 尝试获取原始异常信息，通常这只对本项目的其它组件有效
        String responseBody = ex.getResponseBodyAsString();
        return ResponseEntity
                .status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

}
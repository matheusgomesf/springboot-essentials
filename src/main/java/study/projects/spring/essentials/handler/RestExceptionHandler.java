package study.projects.spring.essentials.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import study.projects.spring.essentials.exception.ExceptionDatails;
import study.projects.spring.essentials.exception.ResourceNotFoundDetails;
import study.projects.spring.essentials.exception.ResourceNotFoundException;
import study.projects.spring.essentials.exception.ValidationExceptionDetails;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDetails> handlerResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        ResourceNotFoundDetails resourceNotFoundDetails = ResourceNotFoundDetails.builder()
                .localDateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .detail(resourceNotFoundException.getMessage())
                .developerMessage(resourceNotFoundException.getClass().getSimpleName())
                .build();
        return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> fieldErrors = exception.getConstraintViolations();

        String filds = fieldErrors.stream().map(ConstraintViolation::getPropertyPath).map(Path::toString).collect(Collectors.joining(", "));
        String fildsMessage = fieldErrors.stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining(", "));

        ValidationExceptionDetails validationExceptionDetails = ValidationExceptionDetails.builder()
                .localDateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Field validation erro")
                .detail(exception.getMessage())
                .developerMessage(exception.getClass().getSimpleName())
                .filds(filds)
                .fildMessage(fildsMessage)
                .build();
        return new ResponseEntity<>(validationExceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionDatails exceptionDatails = ExceptionDatails.builder()
                .localDateTime(LocalDateTime.now())
                .status(status.value())
                .title(ex.getCause().getMessage())
                .detail(ex.getMessage())
                .developerMessage(ex.getClass().getSimpleName())
                .build();
        return new ResponseEntity<>(exceptionDatails, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String filds = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fildsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        ValidationExceptionDetails validationExceptionDetails = ValidationExceptionDetails.builder()
                .localDateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Field validation erro")
                .detail(ex.getMessage())
                .developerMessage(ex.getClass().getSimpleName())
                .filds(filds)
                .fildMessage(fildsMessage)
                .build();
        return new ResponseEntity<>(validationExceptionDetails, HttpStatus.BAD_REQUEST);
    }
}

package study.projects.spring.essentials.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDatails{

    private String filds;
    private String fildMessage;
}

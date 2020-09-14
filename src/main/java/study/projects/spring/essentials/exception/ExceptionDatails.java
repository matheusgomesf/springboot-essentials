package study.projects.spring.essentials.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDatails {

    protected String title;
    protected int status;
    protected String detail;
    protected LocalDateTime localDateTime;
    protected String developerMessage;
}

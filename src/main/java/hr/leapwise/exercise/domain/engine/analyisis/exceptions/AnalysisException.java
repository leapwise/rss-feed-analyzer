package hr.leapwise.exercise.domain.engine.analyisis.exceptions;

import hr.leapwise.exercise.domain.exceptions.CustomException;
import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public class AnalysisException extends CustomException {

    public AnalysisException(ExceptionMessage<String> message) {
        super(message);
    }

    public AnalysisException(ExceptionMessage<String> message, Throwable cause) {
        super(message, cause);
    }
}

package hr.leapwise.exercise.domain.engine.feed.exceptions;

import hr.leapwise.exercise.domain.exceptions.CustomException;
import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public class FeedException extends CustomException {

    public FeedException(ExceptionMessage<String> message) {
        super(message);
    }

    public FeedException(ExceptionMessage<String> message, Throwable cause) {
        super(message, cause);
    }
}

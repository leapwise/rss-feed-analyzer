package hr.leapwise.exercise.domain.processors.exceptions;

import hr.leapwise.exercise.domain.exceptions.CustomException;
import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public class FeedProcessorException extends CustomException {

    public FeedProcessorException(ExceptionMessage<String> message) {
        super(message);
    }

    public FeedProcessorException(ExceptionMessage<String> message, Throwable cause) {
        super(message, cause);
    }
}

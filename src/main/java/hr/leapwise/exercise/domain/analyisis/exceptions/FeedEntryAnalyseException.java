package hr.leapwise.exercise.domain.analyisis.exceptions;

import hr.leapwise.exercise.domain.exceptions.CustomException;
import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public class FeedEntryAnalyseException extends CustomException {

    public FeedEntryAnalyseException(ExceptionMessage<String> message) {
        super(message);
    }

    public FeedEntryAnalyseException(ExceptionMessage<String> message, Throwable cause) {
        super(message, cause);
    }
}

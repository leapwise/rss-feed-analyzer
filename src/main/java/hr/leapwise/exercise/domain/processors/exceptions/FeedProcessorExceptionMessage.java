package hr.leapwise.exercise.domain.processors.exceptions;

import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public enum FeedProcessorExceptionMessage implements ExceptionMessage<String> {

    UNSUPPORTED_TYPE_OF_PROCESSOR("You requested creation of unsupported type of feed processor: %s"),
    INVALID_FEED_URL("Provided feed URL is malformed or invalid: %s"),
    RAW_FEED_PROCESSING_EXCEPTION("Exception occurred in processing of the raw feed")
    ;

    private String message;
    private Object[] parameters;

    FeedProcessorExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return String.format(this.message, parameters);
    }

    @Override
    public FeedProcessorExceptionMessage setParameters(Object... args) {
        this.parameters = args;
        return this;
    }
}

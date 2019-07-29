package hr.leapwise.exercise.domain.feed.exceptions;

import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public enum FeedExceptionMessageCode implements ExceptionMessage<String, FeedExceptionMessageCode> {

    UNSUPPORTED_LANGUAGE("Analysis of this feed is not possible due to unsupported language: $1s");

    private String message;

    FeedExceptionMessageCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public FeedExceptionMessageCode setParameters(Object... args) {
        this.message = String.format(this.message, args);
        return this;
    }
}

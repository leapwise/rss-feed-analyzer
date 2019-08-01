package hr.leapwise.exercise.domain.engine.feed.exceptions;

import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public enum FeedExceptionMessage implements ExceptionMessage<String> {

    UNSUPPORTED_LANGUAGE("Analysis of this feed is not possible due to unsupported language: %s");

    private String message;
    private Object[] parameters;

    FeedExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return String.format(this.message, parameters);
    }

    @Override
    public FeedExceptionMessage setParameters(Object... args) {
        this.parameters = args;
        return this;
    }
}

package hr.leapwise.exercise.domain.feed.exceptions;

import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FeedExceptionMessageCode implements ExceptionMessage<String> {

    UNSUPPORTED_LANGUAGE("Analysis of this feed is not possible due to unsupported language");

    private final String message;

    private final Map<String, FeedExceptionMessageCode> MESSAGES =
            Arrays.stream(values()).collect(Collectors.toMap(FeedExceptionMessageCode::getMessage, Function.identity()));


    FeedExceptionMessageCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public FeedExceptionMessageCode getValue(String message) {
        return MESSAGES.get(message);
    }
}

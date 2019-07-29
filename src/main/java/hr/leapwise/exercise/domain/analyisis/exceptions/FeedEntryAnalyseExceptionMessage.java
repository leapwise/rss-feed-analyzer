package hr.leapwise.exercise.domain.analyisis.exceptions;

import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FeedEntryAnalyseExceptionMessage implements ExceptionMessage<String> {

    DESCRIPTORS_NOT_INITIALIZED("Descritpors are not initialized");

    private final String message;

    private final Map<String, FeedEntryAnalyseExceptionMessage> MESSAGES =
            Arrays.stream(values()).collect(Collectors.toMap(FeedEntryAnalyseExceptionMessage::getMessage, Function.identity()));


    FeedEntryAnalyseExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public FeedEntryAnalyseExceptionMessage getValue(String message) {
        return MESSAGES.get(message);
    }

}

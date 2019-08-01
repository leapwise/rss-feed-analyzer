package hr.leapwise.exercise.domain.exceptions;

public class CustomException extends RuntimeException {

    public CustomException(ExceptionMessage<String> message) {
        super(message.getMessage());
    }

    public CustomException(ExceptionMessage<String> message, Throwable cause) {
        super(message.getMessage(), cause);
    }

}

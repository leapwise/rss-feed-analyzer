package hr.leapwise.exercise.domain.engine.analyisis.exceptions;

import hr.leapwise.exercise.domain.exceptions.ExceptionMessage;

public enum AnalyisisExceptionMessage  implements ExceptionMessage<String> {

    UNSUPPORTED_TYPE_ANALYSER("You requested creation of unsupported type of analyser: %s"),
    AT_LEAST_2_INPUTS_REQUIRED("At least 2 imputs are required for analysis to take place")
    ;

    private String message;
    private Object[] parameters;

    AnalyisisExceptionMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return String.format(this.message, parameters);
    }

    @Override
    public AnalyisisExceptionMessage setParameters(Object... args) {
        this.parameters = args;
        return this;
    }
}

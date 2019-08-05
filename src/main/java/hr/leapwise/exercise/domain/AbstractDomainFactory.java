package hr.leapwise.exercise.domain;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDomainFactory<T> implements DomainFactory<T> {

    @Autowired
    protected List<T> implementations;

    protected Map<Class, T>  initBase() {
        final Map<Class, T> supportedImplementations = new HashMap<>();
        for(T implementation : implementations) {
            supportedImplementations.put(implementation.getClass(), implementation);
        }
        return supportedImplementations;
    }




}

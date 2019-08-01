package hr.leapwise.exercise.service;

import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorException;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeedProcessorFactory implements AbstractFactory<FeedProcessor> {

    @Autowired
    private List<FeedProcessor> processors;

    private static final Map<Class, FeedProcessor> supportedProcessors = new HashMap<>();

    @PostConstruct
    public void init() {
        for(FeedProcessor processor : processors) {
            supportedProcessors.put(processor.getClass(), processor);
        }
    }

    @Override
    public FeedProcessor create(Class type) {
        FeedProcessor processor = supportedProcessors.get(type);
        if(processor == null){
            throw new FeedProcessorException(FeedProcessorExceptionMessage.UNSUPPORTED_TYPE_OF_PROCESSOR.setParameters(type.getSimpleName()));
        }
        return processor;
    }
}

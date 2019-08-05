package hr.leapwise.exercise.domain.engine.analyisis;

import hr.leapwise.exercise.domain.AbstractDomainFactory;
import hr.leapwise.exercise.domain.engine.analyisis.exceptions.AnalyisisExceptionMessage;
import hr.leapwise.exercise.domain.engine.analyisis.exceptions.AnalysisException;
import hr.leapwise.exercise.domain.processors.FeedProcessor;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorException;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorExceptionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnalysisDomainFactory extends AbstractDomainFactory<AnalysisInterpreter> {

    private static final Map<Class, AnalysisInterpreter> supportedInterpretors = new HashMap<>();

    @PostConstruct
    public void init() {
        supportedInterpretors.putAll(super.initBase());
    }

    @Override
    public AnalysisInterpreter create(Class type) {
        AnalysisInterpreter processor = supportedInterpretors.get(type);
        if(processor == null){
            throw new AnalysisException(AnalyisisExceptionMessage.UNSUPPORTED_TYPE_ANALYSER.setParameters(type.getSimpleName()));
        }
        return processor;
    }
}

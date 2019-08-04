package hr.leapwise.exercise.domain.engine.analyisis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(ignoreInvalidFields = true)
public class AnalyserProperties {

    @NotNull
    private Integer threshold;

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}

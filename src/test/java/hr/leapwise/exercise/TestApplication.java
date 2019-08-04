package hr.leapwise.exercise;

import hr.leapwise.exercise.domain.engine.analyisis.AnalyserProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableAutoConfiguration
@EnableConfigurationProperties(value = { AnalyserProperties.class })
public class TestApplication {
}

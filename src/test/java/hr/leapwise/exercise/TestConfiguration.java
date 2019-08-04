package hr.leapwise.exercise;

import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
public @interface TestConfiguration {
}

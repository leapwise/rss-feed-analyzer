package hr.leapwise.exercise.service;

import java.util.Optional;
import java.util.function.Function;

public interface FeedProcessor<T, U, V> {

    Optional<U> process(T input);

    Optional<V> transform(U input);

    static  <T, U, V> FeedProcessor<T, U, V> make(Function<T, Optional<U>> processDef, Function<U, Optional<V>> transformDef) {

        return new FeedProcessor<T, U, V>() {
            public Optional<U> process(T input) { return processDef.apply(input); }
            public Optional<V> transform(U input) { return transformDef.apply(input); }
        };
    }

}

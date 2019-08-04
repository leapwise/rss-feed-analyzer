package hr.leapwise.exercise.domain.engine.analyisis.custom;

import hr.leapwise.exercise.domain.engine.analyisis.AnalyserProperties;
import hr.leapwise.exercise.domain.engine.analyisis.AnalysisInterpreter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomAnalysisInterpreter implements AnalysisInterpreter<List<Pair<Long, Long>>, Map<Pair<Long, Long>, Map<Long, Set<Long>>>, List<Long>> {

    @Autowired
    private AnalyserProperties properties;

    public Map<Pair<Long, Long>, Map<Long, Set<Long>>> analyse(List<Pair<Long, Long>> input) {

        final Map<Pair<Long, Long>,  Map<Long, Set<Long>>> result = new HashMap<>();

        final Map<Long, Long> itemIdentifiers = input.stream().map(Pair::getRight)
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        for (Map.Entry<Long, Long> item : itemIdentifiers.entrySet()) {
            final List<Long> descriptors =
                        input.stream().filter(t -> item.getKey().equals(t.getRight())).distinct().map(Pair::getRight).collect(Collectors.toList());

            final Map<Long, Set<Long>> relatedItems = new HashMap<>();
            for (Long descriptor : descriptors) {

               input.stream()
                       .filter(t -> !item.getKey().equals(t.getRight())).filter(t -> descriptor.equals(t.getRight()))
                       .collect(Collectors.groupingBy(
                                Pair::getRight,
                                Collectors.mapping(Pair::getRight,
                                        Collectors.collectingAndThen(Collectors.toSet(), HashSet::new))))
                       .forEach(
                               (key, value) -> relatedItems.merge(key, value, (v1, v2) ->
                                       Stream.concat(v1.stream(), v2.stream()).collect(Collectors.toSet())));
            }
            result.put(Pair.of(item.getKey(), item.getValue()), relatedItems);
        }
        return Collections.unmodifiableMap(result);
    }

    public List<Long> interpret(Map<Pair<Long, Long>, Map<Long, Set<Long>>> input) {

        return input.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey().getLeft(), entry.getValue().values().stream().flatMap(id -> Stream.of(id.size())).mapToInt(Integer::intValue).sum()))
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Pair::getRight)))
                .limit(3)
                .map(Pair::getLeft)
                .collect(Collectors.toList());
    }
}

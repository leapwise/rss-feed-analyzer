package hr.leapwise.exercise.domain.analyisis.custom;

import hr.leapwise.exercise.domain.analyisis.Assembler;
import hr.leapwise.exercise.domain.analyisis.model.custom.abstracts.CustomtDescriptorsHolder;
import hr.leapwise.exercise.domain.analyisis.model.custom.impl.CustomDescriptor;
import hr.leapwise.exercise.domain.analyisis.model.custom.impl.CustomItem;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeedCustomAssembler extends CustomtDescriptorsHolder implements Assembler<Map<CustomDescriptor, Set<CustomItem>>, Map<CustomDescriptor, Set<CustomItem>>> {

    @Override
    public Map<CustomDescriptor, Set<CustomItem>> assemble(Map<CustomDescriptor, Set<CustomItem>> input) {
        if (!CollectionUtils.isEmpty(input)) {
            significantDescriptors.forEach(
                    (key, value) -> input.merge(key, value, (v1, v2) ->
                            Stream.concat(v1.stream(), v2.stream()).collect(Collectors.toSet())));
        }
        return Collections.unmodifiableMap(significantDescriptors);
    }
}

package hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomDismantled extends CustomDissasambled implements Dismantled<CustomDissasambled> {

    @Override
    public void merge(CustomDissasambled dissasambled) {
        this.significantDescriptors = merge(this.significantDescriptors, dissasambled.getSignificantDescriptors());
        this.feeds.addAll(dissasambled.getFeeds());
    }

    @Override
    public Map<CustomDescriptor, Set<CustomItemModel>> getSignificantDescriptors() {
        return Collections.unmodifiableMap(this.significantDescriptors.entrySet()
                .stream().filter((entry) -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

    }

    public static Map<CustomDescriptor, Set<CustomItemModel>> merge(final Map<CustomDescriptor, Set<CustomItemModel>> firstInput, final Map<CustomDescriptor, Set<CustomItemModel>> secondInput) {
        final Map<CustomDescriptor, Set<CustomItemModel>> resultMap;

        if (!CollectionUtils.isEmpty(firstInput)) {
            if(CollectionUtils.isEmpty(secondInput)) {
                resultMap = firstInput;
            } else {
                final Map<CustomDescriptor, Set<CustomItemModel>> primary;
                if(firstInput.size() < secondInput.size()) {
                    primary = firstInput;
                    resultMap = secondInput;
                } else {
                    primary = secondInput;
                    resultMap = firstInput;
                }
                primary.forEach(
                        (key, value) -> resultMap.merge(key, value, (v1, v2) ->
                                Stream.concat(v1.stream(), v2.stream()).collect(Collectors.toSet())));
            }
        } else {
            resultMap = secondInput;
        }
        return resultMap;
    }

}

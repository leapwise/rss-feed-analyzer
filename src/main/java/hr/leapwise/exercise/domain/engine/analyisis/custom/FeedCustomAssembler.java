package hr.leapwise.exercise.domain.engine.analyisis.custom;

import hr.leapwise.exercise.domain.engine.analyisis.Assembler;
import hr.leapwise.exercise.domain.engine.analyisis.Dissasambler;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts.CustomDescriptorsHolder;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDescriptor;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomItem;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeedCustomAssembler extends CustomDescriptorsHolder implements Assembler<RomeFeedImpl, Map<CustomDescriptor, Set<CustomItem>>> {

    private Dissasambler<RomeFeedImpl, Map<CustomDescriptor, Set<CustomItem>>> dissasambler = new FeedCustomDissasambler();

    @Override
    public Map<CustomDescriptor, Set<CustomItem>> assemble(RomeFeedImpl feed) {
        Map<CustomDescriptor, Set<CustomItem>> dissasembeldFeed = dissasambler.disassemble(feed);
        this.significantDescriptors = merge(this.significantDescriptors, dissasembeldFeed);
        return this.significantDescriptors;
    }

    public static Map<CustomDescriptor, Set<CustomItem>> merge(final Map<CustomDescriptor, Set<CustomItem>> firstInput, final Map<CustomDescriptor, Set<CustomItem>> secondInput) {
        final Map<CustomDescriptor, Set<CustomItem>> resultMap;

        if (!CollectionUtils.isEmpty(firstInput)) {
            if(CollectionUtils.isEmpty(secondInput)) {
                resultMap = firstInput;
            } else {
                final Map<CustomDescriptor, Set<CustomItem>> primary;
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

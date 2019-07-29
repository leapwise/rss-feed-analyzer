package hr.leapwise.exercise.domain.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.analyisis.custom.FeedCustomAssembler;
import hr.leapwise.exercise.domain.analyisis.model.Dismantled;
import hr.leapwise.exercise.domain.analyisis.model.custom.abstracts.CustomtDescriptorsHolder;

import java.util.Map;
import java.util.Set;

public class CustomDismantled extends CustomtDescriptorsHolder implements Dismantled<Map<CustomDescriptor,Set<CustomItem>>> {

    private final FeedCustomAssembler assambler = new FeedCustomAssembler();

    @Override
    public void add(Map<CustomDescriptor, Set<CustomItem>> significantDescriptors) {
        significantDescriptors = this.assambler.assemble(significantDescriptors);
    }
}

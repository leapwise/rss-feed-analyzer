package hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.engine.analyisis.custom.FeedCustomAssembler;
import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts.CustomDescriptorsHolder;

import java.util.Map;
import java.util.Set;

public class CustomDismantled extends CustomDescriptorsHolder implements Dismantled<Map<CustomDescriptor,Set<CustomItem>>> {

    @Override
    public void add(Map<CustomDescriptor, Set<CustomItem>> significantDescriptors) {
        this.significantDescriptors = FeedCustomAssembler.merge(this.significantDescriptors, significantDescriptors);
    }
}

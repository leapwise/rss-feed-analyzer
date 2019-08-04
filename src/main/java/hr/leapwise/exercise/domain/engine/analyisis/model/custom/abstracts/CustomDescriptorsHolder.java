package hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts;

import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDescriptor;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomItemModel;

import java.util.*;

public abstract class CustomDescriptorsHolder {

    protected Map<CustomDescriptor, Set<CustomItemModel>> significantDescriptors = new HashMap<>();

    public Map<CustomDescriptor, Set<CustomItemModel>> getSignificantDescriptors() {
        return significantDescriptors;
    }

    public void setSignificantDescriptors(Map<CustomDescriptor, Set<CustomItemModel>> significantDescriptors) {
        this.significantDescriptors = significantDescriptors;
    }
}

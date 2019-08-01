package hr.leapwise.exercise.domain.engine.analyisis.custom;

import hr.leapwise.exercise.domain.engine.analyisis.Assembler;
import hr.leapwise.exercise.domain.engine.analyisis.Dismantler;
import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDescriptor;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomItem;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class CustomDismantler implements Dismantler<RomeFeedImpl, Map<CustomDescriptor,Set<CustomItem>>> {

    private Assembler<RomeFeedImpl, Map<CustomDescriptor, Set<CustomItem>>> assambler = new FeedCustomAssembler();

    @Override
    public Dismantled<Map<CustomDescriptor, Set<CustomItem>>> dismantle(Stream<RomeFeedImpl> input) {
        final CustomDismantled dismantled = new CustomDismantled();
        input.forEach(
                i -> dismantled.add(assambler.assemble(i))
        );
        return dismantled;
    }
}

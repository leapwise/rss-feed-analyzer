package hr.leapwise.exercise.domain.engine.analyisis.custom;

import hr.leapwise.exercise.domain.engine.analyisis.Dismantler;
import hr.leapwise.exercise.domain.engine.analyisis.Dissasambler;
import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDissasambled;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;

import java.util.stream.Stream;

public class CustomDismantler implements Dismantler<RomeFeedImpl, CustomDissasambled> {

    private Dissasambler<RomeFeedImpl, CustomDissasambled> dissasambler = new FeedCustomDissasambler();

    @Override
    public Dismantled<CustomDissasambled> dismantle(Stream<RomeFeedImpl> input) {
        final CustomDismantled dismantled = new CustomDismantled();
        input.forEach(
                i -> dismantled.merge(dissasambler.disassemble(i))
        );
        return dismantled;
    }
}

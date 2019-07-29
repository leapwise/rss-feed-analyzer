package hr.leapwise.exercise.domain.analyisis;

import hr.leapwise.exercise.domain.analyisis.model.Dismantled;

import java.util.stream.Stream;

public interface Dismantler<T, U extends Dissasambler<T, O>, V extends Assembler<O, O>, O> {

    default Dismantled<O> dismantle(Stream<T> input, U dissasambler, V assembler, Dismantled<O> result) {
        input.forEach(
                i -> result.add(assembler.assemble(dissasambler.disassemble(i)))
        );
        return result;
    }

}

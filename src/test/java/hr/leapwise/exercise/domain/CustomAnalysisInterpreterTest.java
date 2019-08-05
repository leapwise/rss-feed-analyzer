package hr.leapwise.exercise.domain;

import hr.leapwise.exercise.domain.engine.analyisis.custom.CustomAnalysisInterpreter;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomAnalysisInterpreterTest {

    @Test
    public void customAnalysisInterpreterTest() {

        CustomAnalysisInterpreter interpreter = new CustomAnalysisInterpreter();

        List<Pair<Long, Long>> list = Arrays.asList(
                Pair.of(1L, 1L), Pair.of( 1L, 2L),
                Pair.of(2L, 3L), Pair.of( 2L, 4L),
                Pair.of(3L, 5L), Pair.of( 3L, 1L), Pair.of( 3L, 2L),
                Pair.of(4L, 1L), Pair.of( 4L, 3L), Pair.of( 4L, 7L),
                Pair.of(5L, 3L), Pair.of( 5L, 5L), Pair.of( 5L, 6L),
                Pair.of(6L, 3L), Pair.of( 6L, 6L)
        );

        Map<Pair<Long, Long>, Map<Long, Set<Long>>> analysedResults = interpreter.analyse(list);

        assertEquals(7, analysedResults.size());
        assertTrue(analysedResults.containsKey(Pair.of(1L, 3L)));

        Map<Long, Set<Long>> relatedItems = analysedResults.get(Pair.of(1L, 3L));

        // item one is found in descriptors 1, 3 and 4
        //  - in descriptor 1 it's found with item: 2
        //  - in descriptor 3 it's found with item 2 and 5
        //  - in descriptor 4 it's found with item 3 and 7
        assertEquals(3, relatedItems.size());

        assertTrue(relatedItems.containsKey(1L));
        assertEquals(1, relatedItems.get(1L).size());
        assertTrue(relatedItems.get(1L).containsAll(Arrays.asList(2L)));

        assertTrue(relatedItems.containsKey(3L));
        assertEquals(2, relatedItems.get(3L).size());
        assertTrue(relatedItems.get(3L).containsAll(Arrays.asList(2L, 5L)));

        assertTrue(relatedItems.containsKey(4L));
        assertEquals(2, relatedItems.get(4L).size());
        assertTrue(relatedItems.get(4L).containsAll(Arrays.asList(3L, 7L)));


        List<Long> interpretedResults = interpreter.interpret(analysedResults);
        assertEquals(Long.valueOf(3), interpretedResults.get(0));
        assertEquals(Long.valueOf(1), interpretedResults.get(1));
        assertEquals(Long.valueOf(5), interpretedResults.get(2));
    }
}

package hr.leapwise.exercise.repository;

import hr.leapwise.exercise.dao.AnalysisRepository;
import hr.leapwise.exercise.dao.DescriptionRepository;
import hr.leapwise.exercise.dao.ItemRepository;
import hr.leapwise.exercise.dao.ResultRepository;
import hr.leapwise.exercise.domain.entities.*;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class AnalysisResultRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Test
    public void insertAnalysisResultTest() {

        // Different items for different descriptors
        final Item itemOneInDOne = new Item("First item in First descriptor", "some link", "11");

        final Item itemTwoInDOne = new Item("Second item in First descriptor", "some other link", "12");

        final Item itemOneInDTwo = itemOneInDOne;

        final Description descriptionOne = new Description("Description 1");

        final Description descriptionTwo = new Description("Description 2");

        final Feed feed = new Feed("One feed", "some feed link", "123", new Date().toInstant(), "hr");
        final Result result = new Result("One Result", Collections.singletonList(feed));

        final Item firstItem = new Item(itemOneInDOne.getTitle(), itemOneInDOne.getLink(), itemOneInDOne.getGuid());
        itemRepository.save(firstItem);

        final Item secondItem = new Item(itemTwoInDOne.getTitle(), itemTwoInDOne.getLink(), itemTwoInDOne.getGuid());
        itemRepository.save(secondItem);

        descriptionRepository.save(descriptionOne);

        resultRepository.save(result);

        final AnalysisBound firstBound = new AnalysisBound(result.getId(), descriptionOne.getId(), firstItem.getId());
        final AnalysisBound secondBound = new AnalysisBound(result.getId(), descriptionOne.getId(), secondItem.getId());

        final AnalysisResult analysisResultOne = new AnalysisResult(firstBound);
        analysisRepository.save(analysisResultOne);

        final AnalysisResult analysisResultTwo = new AnalysisResult(secondBound);
        analysisRepository.save(analysisResultTwo);

        final Item thirdItem =  new Item(itemOneInDTwo.getTitle(), itemOneInDTwo.getLink(), itemOneInDTwo.getGuid());
        itemRepository.save(thirdItem);

        descriptionRepository.save(descriptionTwo);

        final AnalysisBound thirdBound = new AnalysisBound(result.getId(), descriptionTwo.getId(), thirdItem.getId());

        final AnalysisResult analysisResultTree = new AnalysisResult(thirdBound);
        analysisRepository.save(analysisResultTree);

        List<AnalysisResult> analysisResults = analysisRepository.findAllByResultId(result.getId());

        assertThat(analysisResults, IsIterableWithSize.iterableWithSize(3));

        analysisRepository.deleteAll();
        resultRepository.deleteAll();
        itemRepository.deleteAll();
        descriptionRepository.deleteAll();

    }
}

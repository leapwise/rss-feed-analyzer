package hr.leapwise.exercise.repository;

import hr.leapwise.exercise.dao.*;
import hr.leapwise.exercise.domain.engine.analyisis.custom.CustomAnalysisInterpreter;
import hr.leapwise.exercise.domain.entities.*;
import org.apache.commons.lang3.tuple.Triple;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.management.Descriptor;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoriesTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Test
    public void insertItemUsingSaveTest() {

        final Item initialItem = new Item("Initial item", "http://source.domain.com", "12345");

        itemRepository.save(initialItem);

        List<Item> savedItems = itemRepository.findAll();

        assertThat(savedItems, IsIterableWithSize.iterableWithSize(1));
        assertThat(savedItems.get(0),
                AllOf.allOf(
                        HasPropertyWithValue.hasProperty("title", Is.is(initialItem.getTitle())),
                        HasPropertyWithValue.hasProperty("link", Is.is(initialItem.getLink())),
                        HasPropertyWithValue.hasProperty("guid", Is.is(initialItem.getGuid()))
                ));

        final Item newItemBasedOnInitial = new Item(initialItem.getTitle(), initialItem.getLink(), initialItem.getGuid());

        itemRepository.save(newItemBasedOnInitial);

        savedItems = itemRepository.findAll();

        assertThat(savedItems, IsIterableWithSize.iterableWithSize(2));
        assertThat(savedItems.get(0),
                AllOf.allOf(
                        HasPropertyWithValue.hasProperty("title", Is.is(initialItem.getTitle())),
                        HasPropertyWithValue.hasProperty("link", Is.is(initialItem.getLink())),
                        HasPropertyWithValue.hasProperty("guid", Is.is(initialItem.getGuid()))
                ));
        assertThat(savedItems.get(1),
                AllOf.allOf(
                        HasPropertyWithValue.hasProperty("title", Is.is(newItemBasedOnInitial.getTitle())),
                        HasPropertyWithValue.hasProperty("link", Is.is(newItemBasedOnInitial.getLink())),
                        HasPropertyWithValue.hasProperty("guid", Is.is(newItemBasedOnInitial.getGuid()))
                ));

        itemRepository.deleteAll();
    }

    @Test
    public void insertResultWithTwoFeedsTest() {

        final Feed feed1 = new Feed("Feed one", "http://source.domain.com", "11", new Date().toInstant(), "hr");
        final Feed feed2 = new Feed("Feed two", "http://source.domain.com", "12", new Date().toInstant(), "hr");
        Result result = new Result("1st result",  Arrays.asList(feed1, feed2));

        Result savedResult = resultRepository.save(result);
        List<Feed> savedResultFeeds = savedResult.getFeeds().collect(Collectors.toList());

        assertThat(savedResult, HasPropertyWithValue.hasProperty("code", Is.is("1st result")));
        assertThat(savedResultFeeds, IsIterableWithSize.iterableWithSize(2));
        assertThat(savedResultFeeds.stream().filter(f -> feed1.getTitle().equals(f.getTitle())).findAny().orElse(null),
                AllOf.allOf(
                        HasPropertyWithValue.hasProperty("title", Is.is(feed1.getTitle())),
                        HasPropertyWithValue.hasProperty("link", Is.is(feed1.getLink())),
                        HasPropertyWithValue.hasProperty("lastBuildDate", Is.is(feed1.getLastBuildDate())),
                        HasPropertyWithValue.hasProperty("feedLanguage", Is.is(feed1.getFeedLanguage()))
                ));
        assertThat(savedResultFeeds.stream().filter(f -> feed2.getTitle().equals(f.getTitle())).findAny().orElse(null),
                AllOf.allOf(
                        HasPropertyWithValue.hasProperty("title", Is.is(feed2.getTitle())),
                        HasPropertyWithValue.hasProperty("link", Is.is(feed2.getLink())),
                        HasPropertyWithValue.hasProperty("lastBuildDate", Is.is(feed2.getLastBuildDate())),
                        HasPropertyWithValue.hasProperty("feedLanguage", Is.is(feed2.getFeedLanguage()))
                ));

        // celanup - will cascade delete to feeds
        resultRepository.deleteAll();
    }

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

        CustomAnalysisInterpreter inter = new CustomAnalysisInterpreter();

        List<Triple<Long, Long, Long>> list = Arrays.asList(
            Triple.of(1L, 1L, 1L), Triple.of(1L, 1L, 2L) ,
                Triple.of(1L, 2L, 3L), Triple.of(1L, 2L, 4L),
                Triple.of(1L, 3L, 5L), Triple.of(1L, 3L, 1L), Triple.of(1L, 3L, 2L),
                Triple.of(1L, 4L, 1L), Triple.of(1L, 4L, 3L), Triple.of(1L, 4L, 7L),
                Triple.of(1L, 5L, 3L), Triple.of(1L, 5L, 5L), Triple.of(1L, 5L, 6L),
                Triple.of(1L, 6L, 3L), Triple.of(1L, 6L, 6L)
        );
        inter.interpret(inter.analyse(list));
        // inter.analyse(analysisResults.stream().map(ar -> Triple.of(ar.getResultId(), ar.getDescriptionId(), ar.getItemId())).collect(Collectors.toList()));

        analysisRepository.deleteAll();
        resultRepository.deleteAll();
        itemRepository.deleteAll();
        descriptionRepository.deleteAll();


    }

}

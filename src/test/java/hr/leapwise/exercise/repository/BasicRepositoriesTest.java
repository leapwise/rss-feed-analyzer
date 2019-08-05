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
public class BasicRepositoriesTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ResultRepository resultRepository;

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



}

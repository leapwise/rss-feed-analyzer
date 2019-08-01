package hr.leapwise.exercise.service;

import hr.leapwise.exercise.domain.entities.Item;
import hr.leapwise.exercise.service.domain.ItemService;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    private void insertItems() {
        final Item item1 = new Item();
        item1.setLink("link1");
        item1.setTitle("title1");

        final Item item2 = new Item();
        item2.setLink("link2");
        item2.setTitle("title2");

        itemService.saveOrUpdate(item1);
        itemService.saveOrUpdate(item2);
    }

    @Test
    public void insertItemsTest() {
        insertItems();
    }

    @Test
    public void retrieveItems() {
        insertItems();
        List<Item> items = itemService.getAllItems();
        assertThat(items, IsIterableWithSize.iterableWithSize(2));
    }
}

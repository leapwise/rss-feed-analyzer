package hr.leapwise.exercise.service.domain;

import hr.leapwise.exercise.dao.ItemRepository;
import hr.leapwise.exercise.domain.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        return items;
    }

    public void saveOrUpdate(Item person) {
        itemRepository.save(person);
    }

}

package hr.leapwise.exercise.dao;

import hr.leapwise.exercise.domain.entities.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

}

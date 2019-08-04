package hr.leapwise.exercise.dao;

import hr.leapwise.exercise.domain.entities.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DescriptionRepository extends JpaRepository<Description, Long> {
}

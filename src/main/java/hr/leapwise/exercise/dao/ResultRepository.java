package hr.leapwise.exercise.dao;

import hr.leapwise.exercise.domain.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {
}

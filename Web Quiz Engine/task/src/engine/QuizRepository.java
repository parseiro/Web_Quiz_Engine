package engine;


import engine.Quiz;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuizRepository extends CrudRepository<Quiz, Long> {
    Optional<Quiz> findByTitle(String title);
}

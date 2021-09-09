package engine.repository;

import engine.entity.AppUser;
import engine.entity.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletionRepository extends JpaRepository<Completion, Long> {
    Page<Completion> findAllByUser(AppUser appUser, Pageable pageable);
}

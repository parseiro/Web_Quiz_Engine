package engine.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Map;

@Entity
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long completionId;

    @JoinColumn
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Quiz quiz;

    @JoinColumn
    @ManyToOne
    private AppUser user;

    public AppUser getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Completion{" +
                "quiz=" + quiz +
                ", user=" + user +
                ", completedAt=" + completedAt +
                '}';
    }

    private ZonedDateTime completedAt = ZonedDateTime.now();

    public Completion(Quiz quiz, AppUser user) {
        this.quiz = quiz;
        this.user = user;
    }

    public ZonedDateTime getCompletedAt() {
        return completedAt;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public Completion() {
    }

    @JsonValue
    public Map<String, Object> getName() {
        return Map.of(
                "id", quiz.getId(),
                "completedAt", completedAt.toString()
        );
    }
}

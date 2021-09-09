package engine.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import engine.entity.Quiz;

import javax.persistence.*;

@Entity
public class Option {
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name="quiz_id")
    private Quiz quiz;

    public Option(String text) {
        this.text = text;
    }

    public Option() {
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", quiz=" + quiz +
                '}';
    }
}

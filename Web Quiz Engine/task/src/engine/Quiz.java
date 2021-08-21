package engine;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
public class Quiz {
    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String text;

    @NotNull
    @Size(min = 2)
    private String[] options;

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    /*@NotNull
    @NotEmpty
    @Pattern(regexp = "[^,]{1,50}, [^,]{1,50}.*", message = "At least two items") // at least two items
    private String options;

    public String[] getOptions() {
        return this.options == null ?
                null :
                this.options.split("\\s*,\\s*");
    }

    public void setOptions(String... options) {
        this.options = String.join(", ", options);
    }*/

    //    @Pattern(regexp = "\\[\\d*(,|, )?\\]")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String answer;

    public Integer[] getAnswer() {
//        System.out.printf("Processando a resposta: '%s'%n", this.answer);
        return this.answer == null ? new Integer[0] :
                java.util.regex.Pattern.compile("\\s*,\\s*")
                        .splitAsStream(this.answer)
                        .filter(s -> !s.isBlank())
//                        .peek(x -> System.out.println("Convertendo '" + x +"' em int"))
                        .map(x -> Integer.valueOf(x))
                        .toArray(Integer[]::new);

    }

    public void setAnswer(Integer... answer) {
        if (answer == null) {
            this.answer = "";
        } else {
            this.answer = Arrays.stream(answer)
                    .map(i -> i.toString())
                    .collect(Collectors.joining(", "));
        }

//        System.err.println("Answer: '" + this.answer + "'");
    }

/*    public Quiz(String title, String text, String options) {
        this.title = title;
        this.text = text;
        this.options = options;
    }*/



    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", options='" + getOptions() + '\'' +
                '}';
    }

    public Quiz() {
    }

/*    public Quiz(Long id, String title, String text, String options) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

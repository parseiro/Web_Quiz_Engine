package engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

@Controller
@RestController
@RequestMapping("/api")
@Validated
public class QuizController {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/quizzes")
    public Iterable<Quiz> getAllQuizes() {
        return quizRepository.findAll();
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuizesById(@PathVariable("id") @Min(1) Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/quizzes")
    public Optional<Quiz> post(@Valid @RequestBody Quiz quiz) {
//        logger.info("{}", quiz != null ? quiz : "null");
        return Optional.ofNullable(quizRepository.save(quiz));
    }

    @DeleteMapping("/quizzes/{id}")
    public void deletar(@PathVariable @Min(1) Long id) {
        quizRepository.deleteById(id);
    }

    @PostMapping("/quizzes/{id}/solve")
    public Map<String, Object> solve(@PathVariable @Min(1) Long id, @RequestBody Map<String, List<Integer>> map) {
        var quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var answer = map.get("answer");
//        System.err.println("Objeto tipo: " + answer.getClass());
        Set<Integer> answerSet = Set.of(answer.toArray(Integer[]::new));
//        var answerSet = Set.of(answer.toString().split("^\\[\\s*|\\s*,\\s*|\\s*\\]"));
//        System.err.println("Resposta parseada: " + answerSet);

        Map<String, Object> resposta = new HashMap<>();

        if (answerSet.equals(Set.of(quiz.getAnswer()))) {
            resposta.put("success", true);
            resposta.put("feedback", "Congratulations, you're right!");
        } else {
            resposta.put("success", false);
            resposta.put("feedback", "Wrong answer! Please, try again.");
        }
        return resposta;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody final User user) {
        var existsEmail = userRepository.findByEmailAllIgnoreCase(user.getEmail()).isPresent();

        var savedUser = userRepository.save(user);
        return ResponseEntity.ok().build();
    }


}

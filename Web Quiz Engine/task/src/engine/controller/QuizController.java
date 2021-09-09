package engine.controller;

import engine.entity.AppUser;
import engine.entity.Completion;
import engine.entity.NewUserDto;
import engine.entity.Quiz;
import engine.repository.AppUserRepository;
import engine.repository.CompletionRepository;
import engine.repository.QuizRepository;
import engine.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    AppUserService appUserService;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    CompletionRepository completionRepository;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/quizzes")
    public Iterable<Quiz> getAllQuizes(@RequestParam(defaultValue = "0") Integer page) {
        var paging = PageRequest.of(page, 10);

        return quizRepository.findAll(paging);
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuizesById(@PathVariable("id") @Min(1) Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/quizzes")
    public Optional<Quiz> post(
            Authentication authentication,
            @Valid @RequestBody Quiz quiz
    ) {
//        logger.info("{}", quiz != null ? quiz : "null");

        var username = authentication.getName();
        var user = appUserRepository.findUserByUsername(username).get();

//        log.info("Criando um quiz com username " + username + " e user " + user);

        quiz.setAuthor(user);

        return Optional.ofNullable(quizRepository.save(quiz));
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity delete(
            Authentication authentication,
            @PathVariable Long id
    ) {
        var quizOptional = quizRepository.findById(id);

        if (quizOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var quiz = quizOptional.get();

        var author = quiz.getAuthor();

        var currentUsername = authentication.getName();

        if (author == null || !currentUsername.equalsIgnoreCase(author.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        quizRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quizzes/completed")
    public Iterable<Completion> getCompleted(
            Authentication authentication,
            @RequestParam(defaultValue = "0") @Min(0) Integer page
    ) {


        var paging = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "completedAt"));
        var user = appUserRepository.findUserByUsername(authentication.getName()).get();

        var result = completionRepository.findAllByUser(user, paging);

//        log.info("Completed quizes for '" + user.getUsername() + "':");
//        result.forEach(System.out::println);
        return result;

    }

    @PostMapping("/quizzes/{id}/solve")
    public Map<String, Object> solve(
            Authentication authentication,
            @PathVariable @Min(1) Long id,
            @RequestBody Map<String, List<Integer>> map
    ) {
//        log.info("Solve quizz id " + id + " with data " + map);

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

            var user = appUserRepository.findUserByUsername(authentication.getName()).get();
            var completion = new Completion(quiz, user);
            completionRepository.save(completion);
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
    ResponseEntity register(@RequestBody @Valid NewUserDto nud) {
        String username = nud.getEmail();
        String password = nud.getPassword();

        if (username == null || !username.contains("@") || !username.contains(".")
                || password == null || password.length() < 5)
            return ResponseEntity.badRequest().build();

        try {
            appUserService.register(username, password);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public List<AppUser> users() {
        return appUserService.userList();
    }


}

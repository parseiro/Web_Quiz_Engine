package engine;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 5)
    private String password;

    public Long getId() {
        return id;
    }

/*    public void setId(Long id) {
        this.id = id;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }
}

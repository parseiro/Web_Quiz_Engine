package engine.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class NewUserDto {
//    @Email
//    @NotNull
    private String email;

//    @NotNull
//    @Length(min = 5)
    private String password;

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
}

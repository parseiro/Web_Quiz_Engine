package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

/*    @Autowired
    private PasswordEncoder passwordEncoder;*/

    public AppUser register(String username, String password) {


        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException();
        }

//        String encryptedPassword = passwordEncoder.encode(password);
        String encryptedPassword = password;

        var newUser = new AppUser(username, encryptedPassword);

        var savedUser = appUserRepository.save(newUser);

        return savedUser;
    }

    public List<AppUser> userList() {
        return appUserRepository.findAll();
    }
}

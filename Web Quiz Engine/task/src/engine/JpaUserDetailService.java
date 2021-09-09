package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailService implements UserDetailsService {
    @Autowired
    private AppUserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
//        Supplier<UsernameNotFoundException> s =
//                () -> new UsernameNotFoundException("Problem during authentication!");

        var user = userRepository
                .findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Problem during authentication!")
                );

        return new CustomUserDetails(user);
    }
}

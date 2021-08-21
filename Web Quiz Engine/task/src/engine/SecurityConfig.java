package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Authentication : User --> Roles
    @Override
    protected void configure(final AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user1").password(new BCryptPasswordEncoder().encode("secret1"))
                .roles("USER")
                .and()
                .withUser("admin1").password(new BCryptPasswordEncoder().encode("secret1"))
                .roles("USER", "ADMIN");
    }

    // Authorization : Role -> Access
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .httpBasic()

                .and().authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/*").hasRole("USER")
                .antMatchers("/actuator/shutdown").permitAll()

                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Autowired
    public void initialize(AuthenticationManagerBuilder builder,
                           DataSource dataSource) throws Exception {
        builder
                .jdbcAuthentication()
                .dataSource(dataSource)
                .withUser("dave")
                .password("secret")
                .roles("USER");
    }
}

package oit.is.z3334.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login.permitAll())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/janken/**").authenticated()
                .anyRequest().permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // パスワード「isdev」をbcryptでハッシュ化した値に置き換えること
        String hashedPwd = "{bcrypt}$2a$12$VDYFkFkQpqZg/XW8ugCVteXOq51lW8Puru0t/DgsbM6Kmg6Y/0IJu";

        UserDetails user1 = User.withUsername("user1")
                .password(hashedPwd)
                .roles("USER").build();
        UserDetails user2 = User.withUsername("user2")
                .password(hashedPwd)
                .roles("USER").build();

        UserDetails honda = User.withUsername("ほんだ")
            .password(hashedPwd)
            .roles("USER").build();
        return new InMemoryUserDetailsManager(user1, user2, honda);
    }
}

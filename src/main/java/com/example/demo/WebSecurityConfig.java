package com.example.demo;


import com.example.demo.model.AppUser;
import com.example.demo.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

     private UserDetailsServiceImpl userDetailsService;
     private AppUserRepo appUserRepo;

     @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,AppUserRepo appUserRepo) {
        this.userDetailsService = userDetailsService;
        this.appUserRepo = appUserRepo;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test1").hasRole("USER")
                .antMatchers("/test2").hasRole("ADMIN")
                .antMatchers("/upload").hasRole("ADMIN")
                .antMatchers("/gallery").hasRole("USER")
                .and()
                .formLogin().permitAll()
                .and()
                .csrf().disable();

    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        AppUser appUserUser = new AppUser("MarcinU",passwordEncoder().encode("MarcinU123"),"ROLE_USER");
        AppUser appUserAdmin = new AppUser("MarcinA",passwordEncoder().encode("MarcinA123"),"ROLE_ADMIN");
        appUserRepo.save(appUserUser);
        appUserRepo.save(appUserAdmin);
    }
}

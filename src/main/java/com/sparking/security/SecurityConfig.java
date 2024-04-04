package com.sparking.security;


import com.sparking.repository.AdminRepo;
import com.sparking.repository.BlackListRepo;
import com.sparking.repository.ManagerRepo;
import com.sparking.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepo userRepo;
    private final ManagerRepo managerRepo;
    private final AdminRepo adminRepo;
    private final JWTService jwtService;
    private BlackListRepo blackListRepo;

    @Bean
    public FilterRegistrationBean<JWTFilterUser> jwtFilterUser() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JWTFilterUser(userRepo,jwtService, blackListRepo));
        registrationBean.addUrlPatterns("/api/us/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JWTFilterManager> jwtFilterManger() {
        FilterRegistrationBean<JWTFilterManager> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JWTFilterManager(managerRepo,jwtService, blackListRepo));
        registrationBean.addUrlPatterns("/api/mn/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JWTFilterAdmin> jwtFilterAdmin() {
        FilterRegistrationBean<JWTFilterAdmin> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JWTFilterAdmin(adminRepo,jwtService, blackListRepo));
        registrationBean.addUrlPatterns("/api/ad/*");
        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        //stateless
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}

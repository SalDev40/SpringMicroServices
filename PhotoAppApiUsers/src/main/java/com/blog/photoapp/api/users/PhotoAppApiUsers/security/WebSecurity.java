package com.blog.photoapp.api.users.PhotoAppApiUsers.security;

import com.blog.photoapp.api.users.PhotoAppApiUsers.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    IUserService userService;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    Environment env;

    @Autowired
    public WebSecurity(IUserService userService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            Environment env) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // disable cross site request forgery we will be using JWT
        http.csrf().disable();

        // allow requests sent to this url path
        http.authorizeRequests()
                // .antMatchers("/users/**")
                .antMatchers("/**")
                .permitAll()
                .and()
                // add auth filter to send back JWT
                .addFilter(this.getAuthenticationFilter());

        // allow h2 in memory db
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(userService, env);

        // authenticationManager used in AuthenticationFilter.attemptAuthentication()
        // getAuthenticationManager() used this set authenticationManager()
        filter.setAuthenticationManager(authenticationManager());

        // override default login url by spring framework ("/login")
        // this is what will let spring know to call this filter 
        // only for this url
        filter.setFilterProcessesUrl("/users/login");
        return filter;

    }

    // which service will be used to load user details
    // and which password manager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

}

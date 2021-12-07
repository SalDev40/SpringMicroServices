package com.blog.photoapp.api.users.PhotoAppApiUsers.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blog.photoapp.api.users.PhotoAppApiUsers.service.IUserService;
import com.blog.photoapp.api.users.PhotoAppApiUsers.shared.UserDto;
import com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {

    private IUserService userService;
    private Environment env;

    @Autowired
    public AuthenticationFilter(IUserService userService, Environment env) {
        this.userService = userService;
        this.env = env;

    }

    public AuthenticationFilter(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException {
        try {

            LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);

            // needs to know where/how to find userDetails
            // getAuthenticationManager() will use authenticationManger
            // which comes from setAuthenitcationManger in filter.setAuthenticationManager
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        // username = email
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(username);

        // generate JWT Token
        // String token = Jwts.builder().setSubject(userDetails.getUserId())
        // .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
        // .compact();

        // add token to response header
        response.addHeader("token", "dummy");
        response.addHeader("userId", userDetails.getUserId());

    }

}

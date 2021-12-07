package com.blog.photoapp.api.users.PhotoAppApiUsers.service;

import com.blog.photoapp.api.users.PhotoAppApiUsers.shared.UserDto;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    public UserDto createUser(UserDto dto);

    public UserDto getUserDetailsByEmail(String email);
    public UserDto getUserByUserId(String userId);

}

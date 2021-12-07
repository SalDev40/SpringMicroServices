package com.blog.photoapp.api.users.PhotoAppApiUsers.ui.controller;

import javax.validation.Valid;

import com.blog.photoapp.api.users.PhotoAppApiUsers.service.IUserService;
import com.blog.photoapp.api.users.PhotoAppApiUsers.shared.UserDto;
import com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model.CreateUserRequestModel;
import com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model.CreateUserResponseModel;
import com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model.UserResponseModel;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    private Environment env;
    private IUserService userService;

    @Autowired
    public UsersController(Environment env, IUserService userService) {
        this.env = env;
        this.userService = userService;
    }

    @GetMapping("/sc")
    public String status() {
        return "Hello world from users ON " + env.getProperty("local.server.port");
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel user) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);

        CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping(value = "/{userId}", 
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = userService.getUserByUserId(userId);
        UserResponseModel returnValue = modelMapper.map(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

}

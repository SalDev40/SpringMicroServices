package com.blog.photoapp.api.users.PhotoAppApiUsers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blog.photoapp.api.users.PhotoAppApiUsers.data.AlbumsServiceClient;
import com.blog.photoapp.api.users.PhotoAppApiUsers.data.UserEntity;
import com.blog.photoapp.api.users.PhotoAppApiUsers.repository.IUserRepository;
import com.blog.photoapp.api.users.PhotoAppApiUsers.shared.UserDto;
import com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model.AlbumResponseModel;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import feign.FeignException;

@Service
public class UserServiceImpl implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    IUserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    RestTemplate restTemplate;
    AlbumsServiceClient albumsServiceClient;
    Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public UserServiceImpl(
            IUserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            RestTemplate restTemplate,
            AlbumsServiceClient albumsServiceClient,
            Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restTemplate = restTemplate;
        this.albumsServiceClient = albumsServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        // add id to search db with from frontend
        userDetails.setUserId(UUID.randomUUID().toString());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        // encrypt password with spring security
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        userRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    // authentication filter class will use this to authenticate user
    // UsernamePasswordAuthenticationToken() uses this to get from DB
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null)
            throw new UsernameNotFoundException(username);

        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        List<AlbumResponseModel> albumsList = null;
        try {
            Resilience4JCircuitBreaker circuitbreaker = circuitBreakerFactory
                    .create("albumsServiceClientCircuitBreaker");

            logger.info("Before calling albums Microservice");
            albumsList = circuitbreaker.run(() -> albumsServiceClient.getAlbums(userId));
            logger.info("After calling albums Microservice");

        } catch (FeignException e) {
            logger.error("ERROR");
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        logger.info("\n ALBUMS : " + albumsList.toString());
        userDto.setAlbums(albumsList);

        return userDto;
    }

}

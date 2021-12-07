package com.blog.photoapp.api.users.PhotoAppApiUsers.repository;

import com.blog.photoapp.api.users.PhotoAppApiUsers.data.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}

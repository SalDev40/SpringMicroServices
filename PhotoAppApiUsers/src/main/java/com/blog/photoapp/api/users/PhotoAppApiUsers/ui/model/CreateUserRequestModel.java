package com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {

    @NotNull(message = "first Name can not be null")
    private String firstName;

    @NotNull(message = "last Name can not be null")
    private String lastName;
    
    @NotNull(message = "email can not be null")
    @Email
    private String email;
    
    @NotNull(message = "password can not be null")
    @Size(min=2, message = "password must be larger than 2")
    private String password;


    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
 
}

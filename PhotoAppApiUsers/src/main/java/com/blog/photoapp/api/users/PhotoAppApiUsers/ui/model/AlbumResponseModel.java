package com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model;

public class AlbumResponseModel {
    private String albumId;
    private String userId;
    private String name;
    private String description;

    public AlbumResponseModel() {
        this.albumId = "empty";
        this.userId = "empty";
        this.name = "empty";
        this.description = "empty";
    }

    public AlbumResponseModel(String albumId) {
        this.albumId = albumId;
        this.userId = "feign default";
        this.name = "feign default";
        this.description = "feign default";
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

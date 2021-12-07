package com.blog.photoapp.api.users.PhotoAppApiUsers.data;

import java.util.ArrayList;
import java.util.List;

import com.blog.photoapp.api.users.PhotoAppApiUsers.ui.model.AlbumResponseModel;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "albums-ws", fallback = AlbumsFallBack.class)
public interface AlbumsServiceClient {

    @GetMapping("/albums/user/{id}")
    public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallBack implements AlbumsServiceClient {

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        ArrayList<AlbumResponseModel> fb = new ArrayList<AlbumResponseModel>();
        fb.add(new AlbumResponseModel("default fb"));
        return fb;
    }

}

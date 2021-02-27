package com.example.smartcradleandroidapp;

import com.example.smartcradleandroidapp.model.Posts;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ControllerInterface {
    @GET("ledblink")
    Call<Posts> getPosts();
}

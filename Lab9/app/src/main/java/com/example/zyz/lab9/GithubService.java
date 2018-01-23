package com.example.zyz.lab9;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ZYZ on 2017/12/18.
 */

public interface GithubService{
    /**
     * retrofit对象提供相应的访问Github用户名以及其repos的的API
     */
    @GET("/users/{user}")
    Observable<Github> getUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<List<Repos>> getUserRepos(@Path("user") String user);
}

package com.example.zyz.lab9;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by ZYZ on 2017/12/18.
 */

public final class ServiceFactory {

    public static final String API_URL = "https://api.github.com";

    public interface GithubService{
        /**
         * retrofit对象提供相应的访问Github用户名以及其repos的的API
         */
        @GET("/users/{user}")
        Observable<Github> getUser(@Path("user") String user);

        @GET("users/{user}/repos")
        Observable<List<Repos>> getUserRepos(@Path("user") String user);
    }

    /**
     * 使用特定的参数设置创建一个Retrofit客户端
     * @return 创建的Retrofit客户端
     */
    public static Retrofit createRetrofit()
    {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return new Retrofit.Builder().baseUrl(API_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}

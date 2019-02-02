package com.four_leader.snl.util.retrofitserviceImpl.retrofitservice;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by LeeDoBin on 2019-02-02.
 */

public interface RetrofitInterface {
    @GET("api/unknown")
    Call<Object> doGetListResources();

    @POST("api/users")
    Call<Object> createUser(@Body Object object);

    @GET("api/users?")
    Call<Object> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("api/users?")
    Call<Object> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}

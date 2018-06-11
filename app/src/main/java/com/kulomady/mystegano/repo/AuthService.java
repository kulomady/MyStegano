package com.kulomady.mystegano.repo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author kulomady on 6/10/18.
 */

public interface AuthService {

    @Multipart
    @POST("auth/register")
    Call<User> register(  @Part("file\"; filename=\"test.png ") RequestBody requestBody,
                        @Part("username") RequestBody username,
                        @Part("password") RequestBody password,
                        @Part("email") RequestBody email,
                        @Part("secreet_key") RequestBody secreetKey);

    @POST("auth/login")
    Call<User> login(@Body User user);
}

package com.foodapp.android.foodapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Provide retrofit instance if it exists
// Otherwise it will create a new instance and return it
public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.yummly.com/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

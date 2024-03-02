package com.app.bigprize.Network;

import com.app.bigprize.Value.BIG_Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Big_ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient() {
        if (okHttpClient == null)
            initOkHttp();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BIG_Constants.getAppURL())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES);
//        httpClient.interceptors().add(interceptor);
        okHttpClient = httpClient.build();
    }
}

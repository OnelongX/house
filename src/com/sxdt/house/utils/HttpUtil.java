package com.sxdt.house.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

/**
 * Created by huanglong on 2017/1/4.
 */
public class HttpUtil {
    private final static String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A300 Safari/602.1";

    public static String get(String url) throws IOException {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Request request = new Request.Builder().header("User-Agent", USER_AGENT)
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        if (response.code() == 200) {
            return response.body().string();
        }
        System.err.println("code:"+response.code());
        return "";
    }
}

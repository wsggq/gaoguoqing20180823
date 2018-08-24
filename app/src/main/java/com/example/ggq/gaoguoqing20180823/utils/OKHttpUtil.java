package com.example.ggq.gaoguoqing20180823.utils;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpUtil {
    private static OKHttpUtil okHttpUtil;
    private OkHttpClient okHttpClient;
    private static final String TAG = "OKHttpUtil";

    public static OKHttpUtil getInstance() {
        if (okHttpUtil == null) {
            synchronized (OKHttpUtil.class) {
                if (okHttpUtil == null) {
                    okHttpUtil = new OKHttpUtil();
                }
            }
        }
        return okHttpUtil;
    }

    private OKHttpUtil() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 封装get请求
     *
     * @param url
     * @param params
     * @param requestCallback
     */
    public void getData(String url, HashMap<String, String> params, final RequestCallback requestCallback) {
        StringBuilder stringBuilder = new StringBuilder();
        String allUrl = "";
        for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
            stringBuilder.append("?").append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
        }
        allUrl = url + stringBuilder.toString().substring(0, allUrl.length() - 1);
        Log.d(TAG, "getData: " + allUrl);
        Request request = new Request.Builder()
                .url(allUrl)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(requestCallback != null){
                    requestCallback.failure(call,e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(requestCallback != null){
                    requestCallback.onResponse(call,response);
                }
            }
        });
    }

    /**
     * 封装post请求
     * @param url
     * @param params
     * @param requestCallback
     */
    public void postData(String url, HashMap<String,String> params, final RequestCallback requestCallback){
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && params.size()>0){
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                builder.add(stringStringEntry.getKey(),stringStringEntry.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(requestCallback != null){
                    requestCallback.failure(call,e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(requestCallback != null){
                    requestCallback.onResponse(call, response);
                }
            }
        });
    }
}

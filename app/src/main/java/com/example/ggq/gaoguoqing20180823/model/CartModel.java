package com.example.ggq.gaoguoqing20180823.model;

import android.os.Handler;
import android.text.TextUtils;
import com.example.ggq.gaoguoqing20180823.bean.CartBean;
import com.example.ggq.gaoguoqing20180823.utils.OKHttpUtil;
import com.example.ggq.gaoguoqing20180823.utils.RequestCallback;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Response;

public class CartModel {
    private Handler handler = new Handler();
    /**
     * 请求购物车数据
     * @param url
     * @param params
     * @param cartCallback
     */
    public void getCart(String url, HashMap<String,String> params, final CartCallback cartCallback){
        OKHttpUtil.getInstance().postData(url, params, new RequestCallback() {
            @Override
            public void failure(Call call, IOException e) {
                if(cartCallback != null){
                    cartCallback.failure("网络异常,请稍后再试!");
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == HttpURLConnection.HTTP_OK){
                    try {
                        String result = response.body().string();
                        if(!TextUtils.isEmpty(result)){
                            parseResult(result,cartCallback);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 解析购物车数据
     * @param result
     * @param cartCallback
     */
    private void parseResult(String result, final CartCallback cartCallback) {
        final CartBean cartBean = new Gson().fromJson(result, CartBean.class);
        if(cartCallback != null && cartBean != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    cartCallback.success(cartBean);
                }
            });
        }
    }

    public interface CartCallback{
        void success(CartBean cartBean);
        void failure(String msg);
    }
}

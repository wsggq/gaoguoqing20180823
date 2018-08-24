package com.example.ggq.gaoguoqing20180823.view;

import com.example.ggq.gaoguoqing20180823.bean.CartBean;

public interface CartView {
    void onSuccess(CartBean cartBean);
    void onError(String msg);
}

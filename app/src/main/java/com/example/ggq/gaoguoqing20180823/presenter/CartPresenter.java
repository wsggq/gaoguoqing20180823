package com.example.ggq.gaoguoqing20180823.presenter;

import com.example.ggq.gaoguoqing20180823.bean.CartBean;
import com.example.ggq.gaoguoqing20180823.model.CartModel;
import com.example.ggq.gaoguoqing20180823.view.CartView;
import java.util.HashMap;

public class CartPresenter {
    private CartModel cartModel;
    private CartView cartView;

    public CartPresenter(CartView cartView) {
        cartModel = new CartModel();
        attachView(cartView);
    }

    /**
     * 绑定view到presenter
     * @param cartView
     */
    private void attachView(CartView cartView) {
        this.cartView = cartView;
    }
    //请求购物车数据
    public void getCarts(final String url, HashMap<String,String> params){
        cartModel.getCart(url, params, new CartModel.CartCallback() {
            @Override
            public void success(CartBean cartBean) {
                if (cartView != null) {
                    cartView.onSuccess(cartBean);
                }
            }

            @Override
            public void failure(String msg) {
                if(cartView != null){
                    cartView.onError(msg);
                }
            }
        });
    }

    /**
     * 解除绑定
     */
    public void detachView(){
        this.cartView = null;
    }
}

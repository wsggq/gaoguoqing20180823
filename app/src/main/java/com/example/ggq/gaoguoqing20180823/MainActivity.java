package com.example.ggq.gaoguoqing20180823;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ggq.gaoguoqing20180823.adapter.CarAllCheckListener;
import com.example.ggq.gaoguoqing20180823.adapter.CartAdapter;
import com.example.ggq.gaoguoqing20180823.bean.CartBean;
import com.example.ggq.gaoguoqing20180823.common.Api;
import com.example.ggq.gaoguoqing20180823.presenter.CartPresenter;
import com.example.ggq.gaoguoqing20180823.view.CartView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements XRecyclerView.LoadingListener,CartView, CarAllCheckListener, View.OnClickListener {

    @BindView(R.id.xrecycler_view)
    XRecyclerView xrecyclerView;
    @BindView(R.id.all_check_box)
    CheckBox allCheckBox;
    @BindView(R.id.tv_totalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.btn_account)
    Button btnAccount;
    private List<CartBean.DataBean> list;
    private int page = 1;
    private CartAdapter cartAdapter;
    private CartPresenter cartPresenter;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        loadData();
    }

    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("uid","17505");
        params.put("page",page+"");
        cartPresenter = new CartPresenter(this);
        cartPresenter.getCarts(Api.GETCARTS_URL,params);
    }

    private void initView() {
        list = new ArrayList<>();
        xrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xrecyclerView.setLoadingMoreEnabled(true);
        xrecyclerView.setLoadingListener(this);
        allCheckBox.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        xrecyclerView.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        page++;
        xrecyclerView.loadMoreComplete();
    }

    /**
     * 购物车数据请求成功
     * @param cartBean
     */
    @Override
    public void onSuccess(CartBean cartBean) {
        if(cartBean != null && cartBean.getData() != null){
            if(page == 1){
                list = cartBean.getData();
                cartAdapter = new CartAdapter(this, list);
                xrecyclerView.setAdapter(cartAdapter);
                xrecyclerView.refreshComplete();
            }else {
                if(cartAdapter != null){
                    cartAdapter.addLoadData(cartBean.getData());
                }
                xrecyclerView.loadMoreComplete();
            }
            cartAdapter.setCarAllCheckListener(this);
        }
    }

    /**
     * 数据请求失败
     * @param msg
     */
    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 全选按钮是否选中的回调
     */
    @Override
    public void notifyAllCheckBoxStatus() {
        StringBuilder stringBuilder = new StringBuilder();
        if(cartAdapter != null){
            List<CartBean.DataBean> cartList = cartAdapter.getCartList();
            for (int i = 0; i < cartList.size(); i++) {
                stringBuilder.append(cartList.get(i).isSelected());
                List<CartBean.DataBean.ListBean> list = cartAdapter.getCartList().get(i).getList();
                for (int j = 0; j < list.size(); j++) {
                    stringBuilder.append(list.get(j).isSelected());
                }
            }
        }
        Log.d(TAG, "notifyAllCheckBoxStatus: "+stringBuilder.toString());
        if(stringBuilder.toString().contains("false")){
            allCheckBox.setChecked(false);
        }else {
            allCheckBox.setChecked(true);
        }
        totalPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartPresenter.detachView();
    }

    /**
     * 全选和反选
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(allCheckBox.isChecked()){
            if(list != null && list.size() > 0){
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(true);
                    for (int j = 0; j < list.get(i).getList().size(); j++) {
                        list.get(i).getList().get(j).setSelected(true);
                    }
                }
            }
        }else {
            if(list != null && list.size() > 0){
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                    for (int j = 0; j < list.get(i).getList().size(); j++) {
                        list.get(i).getList().get(j).setSelected(false);
                    }
                }
            }
        }
        cartAdapter.notifyDataSetChanged();
        totalPrice();
    }

    /**
     * 计算总价
     */
    private void totalPrice() {
        double totalPrice = 0;
        for (int i = 0; i < cartAdapter.getCartList().size(); i++) {
            for (int j = 0; j < cartAdapter.getCartList().get(i).getList().size(); j++) {
                CartBean.DataBean.ListBean listBean = cartAdapter.getCartList().get(i).getList().get(j);
                if(listBean.isSelected()){
                    totalPrice+=listBean.getBargainPrice()*listBean.getTotalNum();
                }
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat();
        String total = decimalFormat.format(totalPrice);
        tvTotalPrice.setText("总价:¥"+total);
    }
}

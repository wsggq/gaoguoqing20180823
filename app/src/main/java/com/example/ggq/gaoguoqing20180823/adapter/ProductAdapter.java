package com.example.ggq.gaoguoqing20180823.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ggq.gaoguoqing20180823.R;
import com.example.ggq.gaoguoqing20180823.bean.CartBean;
import com.example.ggq.gaoguoqing20180823.widget.CounterView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<CartBean.DataBean.ListBean> listBeans;
    private CarCheckListener carCheckListener;

    public ProductAdapter(Context context, List<CartBean.DataBean.ListBean> listBeans) {
        this.context = context;
        this.listBeans = listBeans;
    }

    public void setCarCheckListener(CarCheckListener carCheckListener) {
        this.carCheckListener = carCheckListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CartBean.DataBean.ListBean listBean = listBeans.get(position);
        holder.tv_productTitle.setText(listBean.getTitle());
        holder.tv_productPrice.setText("优惠价:¥" + listBean.getBargainPrice());
        String[] split = listBean.getImages().split("\\|");
        Glide.with(context).load(split[0]).into(holder.iv_product);
        //给商家附初值
        holder.product_checkBox.setChecked(listBean.isSelected());
        //点击商家的监听
        holder.product_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.product_checkBox.isChecked()){
                    listBean.setSelected(true);
                }else {
                    listBean.setSelected(false);
                }
                if(carCheckListener != null){
                    carCheckListener.nitifyParent();
                }
            }
        });
        holder.jiaJian_view.setNumEt(listBean.getTotalNum());
        holder.jiaJian_view.setJiaJianListener(new CounterView.JiaJianListener() {
            @Override
            public void getNum(int num) {
                listBean.setTotalNum(num);
                if(carCheckListener != null){
                    carCheckListener.nitifyParent();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_productTitle;
        private TextView tv_productPrice;
        private CheckBox product_checkBox;
        private ImageView iv_product;
        private CounterView jiaJian_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_productTitle = itemView.findViewById(R.id.tv_productTitle);
            tv_productPrice = itemView.findViewById(R.id.tv_productPrice);
            product_checkBox = itemView.findViewById(R.id.product_checkBox);
            iv_product = itemView.findViewById(R.id.iv_product);
            jiaJian_view = itemView.findViewById(R.id.jiaJian_view);
        }
    }
}

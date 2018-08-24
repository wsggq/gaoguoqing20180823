package com.example.ggq.gaoguoqing20180823.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.ggq.gaoguoqing20180823.R;
import com.example.ggq.gaoguoqing20180823.bean.CartBean;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> implements CarCheckListener, View.OnClickListener {

    private Context context;
    private List<CartBean.DataBean> list;
    private CarAllCheckListener carAllCheckListener;

    public CartAdapter(Context context, List<CartBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setCarAllCheckListener(CarAllCheckListener carAllCheckListener) {
        this.carAllCheckListener = carAllCheckListener;
    }
    public void addLoadData(List<CartBean.DataBean> list){
        if(list != null){
            list.addAll(this.list);
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CartBean.DataBean dataBean = list.get(position);
        holder.seller_nameTv.setText(dataBean.getSellerName());
        holder.seller_checkBox.setChecked(dataBean.isSelected());
        holder.recycler_product.setLayoutManager(new LinearLayoutManager(context));
        ProductAdapter productAdapter = new ProductAdapter(context,dataBean.getList());
        holder.recycler_product.setAdapter(productAdapter);
        //商家选中的监听
        productAdapter.setCarCheckListener(this);
        for (int i = 0; i < dataBean.getList().size(); i++) {
            if(dataBean.getList().get(i).isSelected()){
                holder.seller_checkBox.setChecked(true);
            }else {
                holder.seller_checkBox.setChecked(false);
                break;
            }
        }
        //商家的全选反选以及子条目只要有一个没有选中就不选
        holder.seller_checkBox.setOnClickListener(this);
        holder.seller_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.seller_checkBox.isChecked()){
                    dataBean.setSelected(true);
                    List<CartBean.DataBean.ListBean> list = dataBean.getList();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelected(true);
                    }
                }else {
                    dataBean.setSelected(false);
                    List<CartBean.DataBean.ListBean> list = dataBean.getList();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelected(false);
                    }
                }
                notifyDataSetChanged();
                if(carAllCheckListener != null){
                    carAllCheckListener.notifyAllCheckBoxStatus();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }

    @Override
    public void nitifyParent() {
        notifyDataSetChanged();
        if(carAllCheckListener != null){
            carAllCheckListener.notifyAllCheckBoxStatus();
        }
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView seller_nameTv;
        private CheckBox seller_checkBox;
        private RecyclerView recycler_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seller_nameTv = itemView.findViewById(R.id.seller_nameTv);
            seller_checkBox = itemView.findViewById(R.id.seller_checkBox);
            recycler_product = itemView.findViewById(R.id.recycler_product);
        }
    }
    public List<CartBean.DataBean> getCartList(){
        return list;
    }
}

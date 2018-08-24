package com.example.ggq.gaoguoqing20180823.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ggq.gaoguoqing20180823.R;

public class CounterView extends LinearLayout implements View.OnClickListener {
    private TextView jiaTv,jianTv;
    private EditText numEt;
    private int num = 1;

    public CounterView(Context context) {
        this(context, null);
    }

    public CounterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.counter_layout, this, true);
        jiaTv = view.findViewById(R.id.jia);
        jianTv = view.findViewById(R.id.jian);
        numEt = view.findViewById(R.id.jiajian_num);
        numEt.setText(num+"");
        jiaTv.setOnClickListener(this);
        jianTv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jia:
                num++;
                numEt.setText(num+"");
                if(jiaJianListener != null){
                    jiaJianListener.getNum(num);
                }
                break;
            case R.id.jian:
                num--;
                if(num <= 0){
                    Toast.makeText(getContext(), "数量不能小于1", Toast.LENGTH_SHORT).show();
                    num = 1;
                }
                numEt.setText(num+"");
                if(jiaJianListener != null){
                    jiaJianListener.getNum(num);
                }
                break;
        }
    }
    public void setNumEt(int n){
        numEt.setText(n+"");
        num = Integer.parseInt(numEt.getText().toString());
    }
    private JiaJianListener jiaJianListener;

    public void setJiaJianListener(JiaJianListener jiaJianListener) {
        this.jiaJianListener = jiaJianListener;
    }

    public interface JiaJianListener{
        void getNum(int num);
    }
}

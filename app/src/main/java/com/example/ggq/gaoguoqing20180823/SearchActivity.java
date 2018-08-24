package com.example.ggq.gaoguoqing20180823;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ggq.gaoguoqing20180823.widget.CustomView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    String[] name = {"手机", "电脑", "玩具", "手机", "电脑", "苹果手机", "笔记本电脑", "电饭煲 ", "腊肉",
            "特产", "剃须刀", "宝宝", "康佳", "特产", "剃须刀", "宝宝", "康佳"};
    @BindView(R.id.search_input_search)
    EditText searchInputSearch;
    @BindView(R.id.result_search)
    TextView resultSearch;
    @BindView(R.id.search_flowlayout)
    CustomView searchFlowlayout;
    @BindView(R.id.search_clear)
    Button searchClear;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //设置默认显示
        for (int i = 0; i < name.length; i++) {
            textView = new TextView(this);
            textView.setText(name[i]);
            //设置背景
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.setBackground(getDrawable(R.drawable.addatten));
            }
            //设置内边距
            textView.setPadding(5, 5, 5, 5);
            textView.setTextSize(20);
            //设置颜色
            textView.setTextColor(Color.RED);
            //添加数据
            searchFlowlayout.addView(textView);
        }
        //点击搜索添加
        resultSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = searchInputSearch.getText().toString();
                textView = new TextView(SearchActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textView.setBackground(getDrawable(R.drawable.addatten));
                }
                textView.setPadding(5,5,5,5);
                textView.setTextSize(20);
                textView.setText(s);
                searchFlowlayout.addView(textView);
            }
        });
        //删除
        searchClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //mSearchFlowlayout.invalidate();  刷新UI布局
        searchFlowlayout.removeAllViews(); //删除所有
        //mSearchFlowlayout.removeView();   删除单个子控件
    }

    public void ShoppingCart(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }
}

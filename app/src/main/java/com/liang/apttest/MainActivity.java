package com.liang.apttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.liang.anno.MyClass;

import butterknife.BindView;
import butterknife.ButterKnife;

@MyClass
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectActivity.inject(this);//调用build生成的类
        ButterKnife.bind(this);
    }
}

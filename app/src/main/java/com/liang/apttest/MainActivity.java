package com.liang.apttest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.liang.annotations.BindView;
import com.liang.inject.JInjector;
import com.liang.annotations.OnClick;

import com.liang.annotations.OnLongClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        InjectActivity.inject(this);//调用build生成的类
        JInjector.bind(this);
        imageView.setColorFilter(Color.GREEN);
    }

    @OnClick({R.id.button,R.id.imageView})
    public void test() {
        Toast.makeText(this, "test", android.widget.Toast.LENGTH_SHORT).show();
        switch (imageView.getBaseline()){
            case 0:
                break;
        }
    }

    @OnLongClick({R.id.button,R.id.imageView})
    public boolean test2() {
        Toast.makeText(this, "test2", android.widget.Toast.LENGTH_SHORT).show();
        return true;
    }
}

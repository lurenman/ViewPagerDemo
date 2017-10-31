package com.example.lurenman.viewpagerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lurenman.viewpagerdemo.activity.StartpageActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_Startpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_Startpage = (TextView) findViewById(R.id.tv_Startpage);
        tv_Startpage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==tv_Startpage)
        {
            Intent intent = new Intent(MainActivity.this, StartpageActivity.class);
            startActivity(intent);
        }
    }
}

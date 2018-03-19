package com.example.administrator.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.id_cardView)
    Button idCardView;
    @BindView(R.id.id_Notification)
    Button idNotification;
    @BindView(R.id.ToolbarActivity)
    Button ToolbarActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_cardView, R.id.id_Notification, R.id.ToolbarActivity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_cardView:
                startActivity(new Intent(this, CardViewActivity.class));
                break;
            case R.id.id_Notification:
                startActivity(new Intent(this, MyNotificationActivity.class));
                break;
            case R.id.ToolbarActivity:
                startActivity(new Intent(this, ToolbarActivity.class));
                break;
        }
    }


}

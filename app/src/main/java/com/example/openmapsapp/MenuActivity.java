package com.example.openmapsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonYandex;
    private Button buttonOSM;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        buttonOSM = findViewById(R.id.osm);
        buttonYandex = findViewById(R.id.yandex);
        buttonOSM.setOnClickListener(this);
        buttonYandex.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.yandex:
                intent = new Intent(this,YandexActivity.class);
                startActivity(intent);
                break;
            case R.id.osm:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}

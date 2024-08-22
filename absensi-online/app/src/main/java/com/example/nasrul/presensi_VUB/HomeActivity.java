package com.example.nasrul.presensi_VUB;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView img = (ImageView) findViewById(R.id.input);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(a);
            }
        });

        ImageView img1 = (ImageView) findViewById(R.id.daftar);
        img1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,DaftarHadirActivity.class);
                startActivity(i);
            }
        });

        Button buttonLogout = (Button) findViewById(R.id.logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}

package com.mete.template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void takeNote(View view) {
        startActivity(new Intent(this,HandWrite.class));
    }

    public void tasks(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void schedule(View view) {
        startActivity(new Intent(this,Schedule.class));
    }

}

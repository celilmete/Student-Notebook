package com.mete.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class Schedule extends AppCompatActivity {

    TextView[] textViews;

    String[] values;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        values = new String[48];

        textViews = new TextView[48];


        addTextViews();

        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            values = extras.getStringArray("a");
        }
        getValues();


    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_schedule,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.edit_schedule) {
            this.finish();
            startActivity(new Intent(this,ScheduleEditActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void addTextViews() {
        textViews[0] = findViewById(R.id.t1);
        textViews[1] = findViewById(R.id.t2);
        textViews[2] = findViewById(R.id.t3);
        textViews[3] = findViewById(R.id.t4);
        textViews[4] = findViewById(R.id.t5);
        textViews[5] = findViewById(R.id.t6);
        textViews[6] = findViewById(R.id.t7);
        textViews[7] = findViewById(R.id.t8);
        textViews[8] = findViewById(R.id.t9);
        textViews[9] = findViewById(R.id.t10);
        textViews[10] = findViewById(R.id.t11);
        textViews[11] = findViewById(R.id.t12);
        textViews[12] = findViewById(R.id.t13);
        textViews[13] = findViewById(R.id.t14);
        textViews[14] = findViewById(R.id.t15);
        textViews[15] = findViewById(R.id.t16);
        textViews[16] = findViewById(R.id.t17);
        textViews[17] = findViewById(R.id.t18);
        textViews[18] = findViewById(R.id.t19);
        textViews[19] = findViewById(R.id.t20);
        textViews[20] = findViewById(R.id.t21);
        textViews[21] = findViewById(R.id.t22);
        textViews[22] = findViewById(R.id.t23);
        textViews[22] = findViewById(R.id.t24);
        textViews[24] = findViewById(R.id.t25);
        textViews[25] = findViewById(R.id.t26);
        textViews[26] = findViewById(R.id.t27);
        textViews[27] = findViewById(R.id.t28);
        textViews[28] = findViewById(R.id.t29);
        textViews[29] = findViewById(R.id.t30);
        textViews[30] = findViewById(R.id.t31);
        textViews[31] = findViewById(R.id.t32);
        textViews[32] = findViewById(R.id.t33);
        textViews[33] = findViewById(R.id.t34);
        textViews[34] = findViewById(R.id.t35);
        textViews[35] = findViewById(R.id.t36);
        textViews[36] = findViewById(R.id.t37);
        textViews[37] = findViewById(R.id.t38);
        textViews[38] = findViewById(R.id.t39);
        textViews[39] = findViewById(R.id.t40);
        textViews[40] = findViewById(R.id.t41);
        textViews[41] = findViewById(R.id.t42);
        textViews[42] = findViewById(R.id.t43);
        textViews[43] = findViewById(R.id.t44);
        textViews[44] = findViewById(R.id.t45);
        textViews[45] = findViewById(R.id.t46);
        textViews[46] = findViewById(R.id.t47);
        textViews[47] = findViewById(R.id.t48);

    }
    
    public void getValues() {
        int i = 0;
        for (TextView t: textViews) {
            if(values[i] != null && t != null)
                t.setText(values[i]);
            i++;
        }
    }


    
}

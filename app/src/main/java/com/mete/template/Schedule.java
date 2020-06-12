package com.mete.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Schedule extends AppCompatActivity {

    TextView[] textViews;
    EditText[] editTexts;
    String[] values;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        values = new String[48];
        view = getLayoutInflater().inflate(R.layout.edit_schedule,null);

        textViews = new TextView[48];
        editTexts = new EditText[48];

        addTextViews();
        addEditTexts();

        
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_schedule,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.edit_schedule :
                setContentView(R.layout.edit_schedule);
                    break;
            case R.id.save_schedule :
                alignTexts();
                setContentView(R.layout.activity_schedule);
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
    
    public void addEditTexts() {
        editTexts[0] = view.findViewById(R.id.time1);
        editTexts[1] = view.findViewById(R.id.mondayLesson1);
        editTexts[2] = view.findViewById(R.id.tuesdayLesson1);
        editTexts[3] = view.findViewById(R.id.wednesdayLesson1);
        editTexts[4] = view.findViewById(R.id.thursdayLesson1);
        editTexts[5] = view.findViewById(R.id.fridayLesson1);
        editTexts[6] = view.findViewById(R.id.time2);
        editTexts[7] = view.findViewById(R.id.mondayLesson2);
        editTexts[8] = view.findViewById(R.id.tuesdayLesson2);
        editTexts[9] = view.findViewById(R.id.wednesdayLesson2);
        editTexts[10] = view.findViewById(R.id.thursdayLesson2);
        editTexts[11] = view.findViewById(R.id.fridayLesson2);
        editTexts[12] = view.findViewById(R.id.time3);
        editTexts[13] = view.findViewById(R.id.mondayLesson3);
        editTexts[14] = view.findViewById(R.id.tuesdayLesson3);
        editTexts[15] = view.findViewById(R.id.wednesdayLesson3);
        editTexts[16] = view.findViewById(R.id.thursdayLesson3);
        editTexts[17] = view.findViewById(R.id.fridayLesson3);
        editTexts[18] = view.findViewById(R.id.time4);
        editTexts[19] = view.findViewById(R.id.mondayLesson4);
        editTexts[20] = view.findViewById(R.id.tuesdayLesson4);
        editTexts[21] = view.findViewById(R.id.wednesdayLesson4);
        editTexts[22] = view.findViewById(R.id.thursdayLesson4);
        editTexts[23] = view.findViewById(R.id.fridayLesson4);
        editTexts[24] = view.findViewById(R.id.time5);
        editTexts[25] = view.findViewById(R.id.mondayLesson5);
        editTexts[26] = view.findViewById(R.id.tuesdayLesson5);
        editTexts[27] = view.findViewById(R.id.wednesdayLesson5);
        editTexts[28] = view.findViewById(R.id.thursdayLesson5);
        editTexts[29] = view.findViewById(R.id.fridayLesson5);
        editTexts[30] = view.findViewById(R.id.time6);
        editTexts[31] = view.findViewById(R.id.mondayLesson6);
        editTexts[32] = view.findViewById(R.id.tuesdayLesson6);
        editTexts[33] = view.findViewById(R.id.wednesdayLesson6);
        editTexts[34] = view.findViewById(R.id.thursdayLesson6);
        editTexts[35] = view.findViewById(R.id.fridayLesson6);
        editTexts[36] = view.findViewById(R.id.time7);
        editTexts[37] = view.findViewById(R.id.mondayLesson7);
        editTexts[38] = view.findViewById(R.id.tuesdayLesson7);
        editTexts[39] = view.findViewById(R.id.wednesdayLesson7);
        editTexts[40] = view.findViewById(R.id.thursdayLesson7);
        editTexts[41] = view.findViewById(R.id.fridayLesson7);
        editTexts[42] = view.findViewById(R.id.time8);
        editTexts[43] = view.findViewById(R.id.mondayLesson8);
        editTexts[44] = view.findViewById(R.id.tuesdayLesson8);
        editTexts[45] = view.findViewById(R.id.wednesdayLesson8);
        editTexts[46] = view.findViewById(R.id.thursdayLesson8);
        editTexts[47] = view.findViewById(R.id.fridayLesson8);

    }
    
    public void alignTexts() {
        int i = 0;
        for (EditText e:editTexts) {
            values[i] = e.getText().toString();
            i++;
        }
    }
    
}

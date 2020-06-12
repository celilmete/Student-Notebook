package com.mete.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class ScheduleEditActivity extends AppCompatActivity {
    String[] values;
    EditText[] editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        values = new String[48];
        editTexts = new EditText[48];
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

        if (item.getItemId() == R.id.save_schedule) {
            alignTexts();
            Intent intent = new Intent(this,Schedule.class);
            intent.putExtra("a",values);
            this.finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addEditTexts() {
        editTexts[0] =  findViewById(R.id.time1);
        editTexts[1] =  findViewById(R.id.mondayLesson1);
        editTexts[2] =  findViewById(R.id.tuesdayLesson1);
        editTexts[3] =  findViewById(R.id.wednesdayLesson1);
        editTexts[4] =  findViewById(R.id.thursdayLesson1);
        editTexts[5] =  findViewById(R.id.fridayLesson1);
        editTexts[6] =  findViewById(R.id.time2);
        editTexts[7] =  findViewById(R.id.mondayLesson2);
        editTexts[8] =  findViewById(R.id.tuesdayLesson2);
        editTexts[9] =  findViewById(R.id.wednesdayLesson2);
        editTexts[10] = findViewById(R.id.thursdayLesson2);
        editTexts[11] = findViewById(R.id.fridayLesson2);
        editTexts[12] = findViewById(R.id.time3);
        editTexts[13] = findViewById(R.id.mondayLesson3);
        editTexts[14] = findViewById(R.id.tuesdayLesson3);
        editTexts[15] = findViewById(R.id.wednesdayLesson3);
        editTexts[16] = findViewById(R.id.thursdayLesson3);
        editTexts[17] = findViewById(R.id.fridayLesson3);
        editTexts[18] = findViewById(R.id.time4);
        editTexts[19] = findViewById(R.id.mondayLesson4);
        editTexts[20] = findViewById(R.id.tuesdayLesson4);
        editTexts[21] = findViewById(R.id.wednesdayLesson4);
        editTexts[22] = findViewById(R.id.thursdayLesson4);
        editTexts[23] = findViewById(R.id.fridayLesson4);
        editTexts[24] = findViewById(R.id.time5);
        editTexts[25] = findViewById(R.id.mondayLesson5);
        editTexts[26] = findViewById(R.id.tuesdayLesson5);
        editTexts[27] = findViewById(R.id.wednesdayLesson5);
        editTexts[28] = findViewById(R.id.thursdayLesson5);
        editTexts[29] = findViewById(R.id.fridayLesson5);
        editTexts[30] = findViewById(R.id.time6);
        editTexts[31] = findViewById(R.id.mondayLesson6);
        editTexts[32] = findViewById(R.id.tuesdayLesson6);
        editTexts[33] = findViewById(R.id.wednesdayLesson6);
        editTexts[34] = findViewById(R.id.thursdayLesson6);
        editTexts[35] = findViewById(R.id.fridayLesson6);
        editTexts[36] = findViewById(R.id.time7);
        editTexts[37] = findViewById(R.id.mondayLesson7);
        editTexts[38] = findViewById(R.id.tuesdayLesson7);
        editTexts[39] = findViewById(R.id.wednesdayLesson7);
        editTexts[40] = findViewById(R.id.thursdayLesson7);
        editTexts[41] = findViewById(R.id.fridayLesson7);
        editTexts[42] = findViewById(R.id.time8);
        editTexts[43] = findViewById(R.id.mondayLesson8);
        editTexts[44] = findViewById(R.id.tuesdayLesson8);
        editTexts[45] = findViewById(R.id.wednesdayLesson8);
        editTexts[46] = findViewById(R.id.thursdayLesson8);
        editTexts[47] = findViewById(R.id.fridayLesson8);

    }

    public void alignTexts() {
        int i = 0;
        for (EditText e:editTexts) {
            values[i] = e.getText().toString();
            i++;
        }
    }

}
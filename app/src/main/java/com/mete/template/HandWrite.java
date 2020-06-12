package com.mete.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HandWrite extends AppCompatActivity {
    PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_write);

        paintView = findViewById(R.id.view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            paintView.clear();
        }
        switch (item.getItemId()) {

            case R.id.clear:
                paintView.clear();
                break;
            case R.id.save:
                saveScreentshot();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void saveScreentshot() {
        File folder = new File(Environment.getExternalStorageDirectory().getPath().toString() + "/Student Notebook");
        boolean success = false;
        System.out.println(Environment.getExternalStorageDirectory().getPath().toString());
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        System.out.println(success + "folder");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm", Locale.US);
        Date date = new Date();
        String fileName = formatter.format(date) + ".png";
        File file = new File((Environment.getExternalStorageDirectory().getPath().toString()) + "/Student Notebook/" + fileName);
        if (!file.exists()) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                System.out.println("lol");
                e.printStackTrace();
            }
        }
        System.out.println(success + "file");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            System.out.println(outputStream);
            View targetView = paintView;
            Bitmap well = paintView.getBitmap();
            Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Canvas now = new Canvas(save);
            now.drawRect(new Rect(0, 0, 320, 480), paint);
            now.drawBitmap(well, new Rect(0, 0, well.getWidth(), well.getHeight()), new Rect(0, 0, 320, 480), null);
            if (save == null) {
                System.out.println("NULL bitmap save\n");
            }
            System.out.println("lol");
            save.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            scanFile(file.getAbsolutePath());
        } catch (NullPointerException e) {
            System.out.println("lol");
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            System.out.println("lol");

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            System.out.println("lol");

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "IO error", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void scanFile(String path) {

        MediaScannerConnection.scanFile(HandWrite.this,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
}


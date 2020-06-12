package com.mete.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HandWrite extends AppCompatActivity {
    PaintView paintView;
    String serverIP = "167.99.9.212";
    String port = "5000";
    private NotesDao dao;
    Long date;
    Note temp;
    String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_write);

        paintView = findViewById(R.id.view);
        dao = NotesDB.getInstance(this).notesDao();
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
                paintView.clear();
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
            selectedImagePath = file.getAbsolutePath();
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
        System.out.println("path is" + selectedImagePath);
        connectServer();
//        finish();
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

    //file upload

    public void connectServer() {
//        EditText ipv4AddressView = findViewById(R.id.IPAddress);
        String ipv4Address = serverIP;
//        EditText portNumberView = findViewById(R.id.portNumber);
        String portNumber = port;

        String postUrl = "http://" + ipv4Address + ":" + portNumber + "/";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Read BitMap by file path
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .build();

        TextView responseText = findViewById(R.id.responseText);
//        responseText.setText("Please wait ...");

        postRequest(postUrl, postBodyImage);
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseText = findViewById(R.id.responseText);
                        responseText.setText("Failed to Connect to Server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView responseText = findViewById(R.id.responseText);
                        try {
                            String responseString = new String(response.body().string());
//                            clipboard.setPrimaryClip(ClipData.newPlainText("text", responseString));
//                            Toast.makeText(getApplicationContext(),
//                                    "Copied to Clipboard",
//                                    Toast.LENGTH_SHORT)
//                                    .show();
                            date = new Date().getTime();
                            temp = new Note(responseString, date);
                            dao.insertNote(temp);
//                            responseText.setText(responseString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }
}


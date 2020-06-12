package com.mete.template;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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

import com.mete.template.EditNoteActivity;

public class UploadPhoto extends AppCompatActivity {
    String selectedImagePath;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    //    ClipData clipData;
    ClipboardManager clipboard;
    private NotesDao dao;
    Long date;
    Note temp;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        dao = NotesDB.getInstance(this).notesDao();
    }

    public void connectServer(View v) {
        EditText ipv4AddressView = findViewById(R.id.IPAddress);
        String ipv4Address = ipv4AddressView.getText().toString();
        EditText portNumberView = findViewById(R.id.portNumber);
        String portNumber = portNumberView.getText().toString();

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
        responseText.setText("Please wait ...");

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
                        TextView responseText = findViewById(R.id.response_body);
                        try {
                            String responseString = new String(response.body().string());
                            clipboard.setPrimaryClip(ClipData.newPlainText("text", responseString));
                            Toast.makeText(getApplicationContext(),
                                    "Copied to Clipboard",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            date = new Date().getTime();
                            temp = new Note(responseString, date);
                            dao.insertNote(temp);
                            responseText.setText(responseString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
    }


    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == CAMERA_REQUEST && resCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
//            Log.e("URI",imageUri.toString());
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//            saving image
            saveImage(imageBitmap);
        } else {
            super.onActivityResult(reqCode, resCode, data);
            if (resCode == RESULT_OK && data != null) {
                Uri uri = data.getData();

                selectedImagePath = getPath(getApplicationContext(), uri);
                EditText imgPath = findViewById(R.id.imgPath);
                imgPath.setText(selectedImagePath);
//            Toast.makeText(getApplicationContext(), selectedImagePath, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveImage(Bitmap bitmap) {
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
            Bitmap well = bitmap;
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
//        finish();
    }

    public void scanFile(String path) {

        MediaScannerConnection.scanFile(UploadPhoto.this,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }

    // Implementation of the getPath() method and all its requirements is taken from the StackOverflow Paul Burke's answer: https://stackoverflow.com/a/20559175/5426539
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public void takePic(View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
//    public void takePic2(View view){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
//                String.valueOf(System.currentTimeMillis()) + ".jpg"));
//        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//    }

//    public void dispatchTakePictureIntent(View view) {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                ex.printStackTrace();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

}
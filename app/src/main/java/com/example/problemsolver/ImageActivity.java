package com.example.problemsolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.problemsolver.Registration.RegisteredPerson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.core.app.ActivityCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageActivity extends AppCompatActivity {


    private String token;
    private SharedPreferences settings;


    //Объявляем используемые переменные:
    private ImageView imageView;
    private Button PickImage;
    private final int Pick_image = 1;
    Bitmap selectedImage;

    private final Context mContext = this;
    private final String API_URL_BASE = "http://serverip:port";
    private final String LOG_TAG = "BNK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        requestMultiplePermissions();

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT", "");


        imageView = findViewById(R.id.imageView);
        PickImage = findViewById(R.id.button);


        PickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");

                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = imageReturnedIntent.getData();
                    if (imageUri != null) {
                        uploadFile(imageUri); // uploads the file to the web service
                    }
                    //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    //selectedImage = BitmapFactory.decodeStream(imageStream);
                    //imageView.setImageBitmap(selectedImage);
                }
        }
    }

    private void uploadFile(Uri imageUri) {
        String imagePath = getRealPathFromUri(imageUri);
        showMessage(imagePath);
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                ApplicationService.getInstance()
                        .getJSONApi()
                        .uploadFile(token, body)
                        .enqueue(new Callback<Photo>() {
                            @Override
                            public void onResponse(@NonNull Call<Photo> call, @NonNull Response<Photo> response) {
                                if (response.isSuccessful()){
                                    //запрос выполнился успешно
                                    showMessage("Фото отправлено успешно");
                                }
                                else {
                                    //сервер вернул ошибку
                                    showMessage("Фото не отправлено, сервер вернул ошибку");
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<Photo> call, @NonNull Throwable t) {
                                showMessage(t.toString());
                            }
                        });
            }
        }
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    public String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(mContext, uri)) {
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

                return getDataColumn(mContext, contentUri, null, null);
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

                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(mContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
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
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },
                1);
    }
}

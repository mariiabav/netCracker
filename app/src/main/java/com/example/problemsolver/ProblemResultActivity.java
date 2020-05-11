package com.example.problemsolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.problemsolver.event.Model.Event;
import com.example.problemsolver.event.Model.Problem;
import com.example.problemsolver.profile.ProfileActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProblemResultActivity extends AppCompatActivity {

    private Button resultBtn;
    private EditText resultComment;
    private ImageView resultPhoto, resultPhoto1, resultPhoto2, resultPhoto3;
    private String token;
    private SharedPreferences settings;

    private final int Pick_image = 1;
    private ImageButton pickImageBtn;
    private String pictureId;
    private Bitmap selectedImage;

    private List<String> pictures = new ArrayList<>();

    private boolean photo1 = false, photo2 = false, photo3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_result);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT", "");
        String problemId = getIntent().getStringExtra("problem_id");
        Problem problem = new Problem(problemId);

        resultBtn = findViewById(R.id.btn_result);
        resultComment = findViewById(R.id.problem_result_comment);

        pickImageBtn = findViewById(R.id.problem_photo_btn);

        resultPhoto1 = findViewById(R.id.imageView_result1);
        resultPhoto2 = findViewById(R.id.imageView_result2);
        resultPhoto3 = findViewById(R.id.imageView_result3);


        pickImageBtn.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
        });

        resultBtn.setOnClickListener(view -> {
            Event event = new Event(resultComment.getText().toString(), "solved", problem, pictures);
            solvedProblemRequest(event);
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
                        showMessage("Фото загружается");
                        resultBtn.setClickable(false);
                        uploadFile(imageUri);

                    }
                    try {
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        if (!photo1){
                            photo1 = true;
                            resultPhoto1.setImageBitmap(selectedImage);
                        } else if (!photo2) {
                            photo2 = true;
                            resultPhoto2.setImageBitmap(selectedImage);
                        } else if (!photo3) {
                            photo3 = true;
                            resultPhoto3.setImageBitmap(selectedImage);
                        }
                        //resultPhoto.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        resultPhoto.setImageResource(R.drawable.status_pic_unsolved); //каринка "невозмонжо отобразить"
                    }
                }
        }
    }

    private void uploadFile(Uri imageUri) {
        String imagePath = Parser.getRealPathFromUri(imageUri, this);
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
                                    pictureId = response.body().getUrl();
                                    pictures.add(pictureId);
                                }
                                else {

                                }
                                resultBtn.setClickable(true);
                            }
                            @Override
                            public void onFailure(@NonNull Call<Photo> call, @NonNull Throwable t) {

                            }
                        });
            }
        }
    }


    private void solvedProblemRequest(Event event) {
        ApplicationService.getInstance()
                .getJSONApi()
                .createEvent(token, event)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        showMessage("Решение проблемы успешно отправлено");
                        Intent intent = new Intent(ProblemResultActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

}

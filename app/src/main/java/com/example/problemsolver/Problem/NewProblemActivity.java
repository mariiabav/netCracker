package com.example.problemsolver.Problem;

import androidx.annotation.NonNull;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Map.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.Map.Models.DistrictResponse.FeatureMember;
import com.example.problemsolver.MapService;
import com.example.problemsolver.Problem.model.Address;
import com.example.problemsolver.Problem.model.Area;
import com.example.problemsolver.Problem.model.NewProblem;
import com.example.problemsolver.Problem.model.Owner;
import com.example.problemsolver.R;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectDragListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProblemActivity extends Activity {


    private ImageView imageView;
    private ImageButton PickImage;
    private final int Pick_image = 1;
    Bitmap selectedImage;


    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";

    private MapView mapView;
    private LocationManager locationManager;
    private double myLatitude;
    private double myLongitude;

    private String format = "json";
    private Point myLocation;
    private PlacemarkMapObject placemarkMapObject;

    private String[] splitedAddress;
    private String street;
    private String building;

    private Button problem;
    private EditText type, description;
    private String problemType, problemDescription;

    private String address;
    private String coordinates;
    private String adminAreaName;

    private String token;
    private String personId;
    private SharedPreferences settings;

    TextView layoutAddress;

    private MapService mapService;

    private final String API_KEY = "7e3eee55-cf92-4361-919e-e1666d3df1d1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT", "");
        personId = settings.getString("id","");
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_new_problem);

        mapService = MapService.getInstance();

        locationManager = MapKitFactory.getInstance().createLocationManager();

        locationManager.requestSingleUpdate(new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                myLongitude = location.getPosition().getLongitude();
                myLatitude = location.getPosition().getLatitude();
                myLocation = new Point(myLatitude, myLongitude);
                placemarkMapObject = mapView.getMap().getMapObjects().addPlacemark(myLocation, ImageProvider.fromResource(getApplicationContext(), R.drawable.red_geo_point));
                placemarkMapObject.setDraggable(true);
                mapView.getMap().move(new CameraPosition(myLocation, 25.0f, 0.0f, 0.0f));
                placemarkMapObject.setDragListener(new MapObjectDragListener() {
                    @Override
                    public void onMapObjectDragStart(@NonNull MapObject mapObject) {

                    }

                    @Override
                    public void onMapObjectDrag(@NonNull MapObject mapObject, @NonNull Point point) {
                    }

                    @Override
                    public void onMapObjectDragEnd(@NonNull MapObject mapObject) {
                        coordinates = placemarkMapObject.getGeometry().getLongitude() + ", " + placemarkMapObject.getGeometry().getLatitude();
                        mapService.getJSONApi()
                                .getDistrictName(token, API_KEY, format, coordinates)
                                .enqueue(new Callback<DistrictResponse>() {

                                    @Override
                                    public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                                        List<FeatureMember> featureMembers = response.body().getResponse().getGeoObjectCollection()
                                                .getFeatureMember();
                                        address = response.body().getResponse()
                                                .getGeoObjectCollection()
                                                .getFeatureMember()
                                                .get(0)
                                                .getGeoObject()
                                                .getMetaDataProperty()
                                                .getGeocoderMetaData()
                                                .getText();

                                        layoutAddress.setText(address);
                                    }

                                    @Override
                                    public void onFailure(Call<DistrictResponse> call, Throwable t) {

                                    }
                                });
                    }
                });
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });

        problem = findViewById(R.id.btn_problem);
        type = findViewById(R.id.problem_input_name);
        description = findViewById(R.id.problem_input_description);
        mapView = findViewById(R.id.map);
        layoutAddress = findViewById(R.id.query_address);

        imageView = findViewById(R.id.imageView);
        PickImage = findViewById(R.id.problem_photo_btn);

        PickImage.setOnClickListener(v -> {
            //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

            //Тип получаемых объектов - image:
            photoPickerIntent.setType("image/*");

            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
            startActivityForResult(photoPickerIntent, Pick_image);
        });

        problem.setOnClickListener(view -> {
            showMessage("нажалась");
            problemType = type.getText().toString();
            problemDescription = description.getText().toString();
            coordinates = placemarkMapObject.getGeometry().getLongitude() + ", " + placemarkMapObject.getGeometry().getLatitude();
            showMessage(coordinates);
            mapService
                    .getJSONApi()
                    .getDistrictName(token, API_KEY, format, coordinates)
                    .enqueue(new Callback<DistrictResponse>() {
                        @Override
                        public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                            List<FeatureMember> featureMembers = response.body().getResponse().getGeoObjectCollection()
                                    .getFeatureMember();
                            address = response.body().getResponse()
                                    .getGeoObjectCollection()
                                    .getFeatureMember()
                                    .get(0)
                                    .getGeoObject()
                                    .getMetaDataProperty()
                                    .getGeocoderMetaData()
                                    .getText();

                            for (FeatureMember featureMember : featureMembers) {
                                if (featureMember.getGeoObject()
                                        .getMetaDataProperty()
                                        .getGeocoderMetaData()
                                        .getKind().contains("district")) {
                                    adminAreaName = featureMember.getGeoObject()
                                            .getMetaDataProperty()
                                            .getGeocoderMetaData()
                                            .getAddressDetails()
                                            .getCountry()
                                            .getAdministrativeArea()
                                            .getLocality()
                                            .getDependentLocality()
                                            .getDependentLocalityName();
                                    if (adminAreaName.split(" ")[1].equals("район")) {
                                        break;
                                    }
                                }
                            }
                            showMessage(adminAreaName);
                            showMessage(address);

                            splitedAddress = address.split(", ");
                            street = splitedAddress[2];
                            if (splitedAddress.length <= 3) {
                                building = "Общественное место";
                            } else {
                                building = splitedAddress[3];
                            }

                            Area area = new Area(adminAreaName);
                            Address fullAddress = new Address(street, building, area);
                            Owner owner = new Owner(personId);
                            NewProblem newProblem = new NewProblem(fullAddress, problemType, problemDescription, "created", 0, coordinates, owner);

                            ApplicationService.getInstance()
                                    .getJSONApi()
                                    .postNewProblemData(token, newProblem)
                                    .enqueue(new Callback<NewProblem>() {
                                        @Override
                                        public void onResponse(@NonNull Call<NewProblem> call, @NonNull Response<NewProblem> response) {
                                            if (response.isSuccessful()) {
                                                showMessage("Проблема отправлена успешно");
                                            } else {
                                                showMessage("Проблема не отправлена");
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<NewProblem> call, @NonNull Throwable t) {
                                            showMessage("Ошибка во время выполнения запроса");
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(@NotNull Call<DistrictResponse> call, @NotNull Throwable t) {
                            //ошибка во время выполнения запроса
                        }
                    });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}

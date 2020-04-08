package com.example.problemsolver.Organization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.MapService;

import com.example.problemsolver.Map.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.Map.Models.DistrictResponse.FeatureMember;


import com.example.problemsolver.R;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrganizationActivity extends AppCompatActivity {

    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";
    private final int RESULT_NUMBER_LIMIT = 5;

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

    private EditText name, description, email, number;
    private Button registerOrg;

    private String orgName, orgDescription, orgEmail, orgPhone;

    private String address;
    private String coordinates;
    private String adminAreaName;

    private ApplicationService applicationService;
    private MapService mapService;

    private final String API_KEY = "7e3eee55-cf92-4361-919e-e1666d3df1d1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkLocationPermission() == false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_new_organization);

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
                //showMessage(placemarkMapObject.getGeometry().getLatitude() + "");
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });

        name = findViewById(R.id.org_input_name);
        description = findViewById(R.id.org_input_desc);
        email = findViewById(R.id.org_input_email);
        number = findViewById(R.id.org_input_number);
        mapView = findViewById(R.id.map);

        registerOrg = findViewById(R.id.btn_signup);

        registerOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("нажалась");


                orgName = name.getText().toString();
                orgDescription = description.getText().toString();
                orgEmail = email.getText().toString();
                orgPhone = number.getText().toString();


                coordinates = placemarkMapObject.getGeometry().getLongitude() + ", " + placemarkMapObject.getGeometry().getLatitude();
                showMessage(coordinates);

                mapService
                        .getJSONApi()
                        .getDistrictName(API_KEY, format, coordinates)
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
                                        if(adminAreaName.split(" ")[1].equals("район")) {
                                            break;
                                        }
                                    }

                                }
                                showMessage(adminAreaName);
                                showMessage(address);

                                splitedAddress = address.split(", ");
                                street = splitedAddress[2];
                                if(splitedAddress.length <= 3) {
                                    building = "Общественное место";
                                }
                                else {
                                    building = splitedAddress[3];
                                }

                                Area area = new Area(adminAreaName);
                                Address fullAddress = new Address(street, building, area);

                                RegisteredOrganization registeredOrganization = new RegisteredOrganization(fullAddress, orgName, orgDescription, orgEmail, orgPhone);


                                ApplicationService.getInstance()
                                        .getJSONApi()
                                        .postRegistedOrgData(registeredOrganization)
                                        .enqueue(new Callback<RegisteredOrganization>() {
                                            @Override
                                            public void onResponse(@NonNull Call<RegisteredOrganization> call, @NonNull Response<RegisteredOrganization> response) {
                                                if (response.isSuccessful()){
                                                    //запрос выполнился успешно
                                                    showMessage("Регистрация организации вполнена успешно");
                                                }
                                                else {
                                                    //сервер вернул ошибку
                                                    showMessage("Регистрация организации не вполнена");
                                                }
                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<RegisteredOrganization> call, @NonNull Throwable t) {
                                                showMessage("Ошибка во время выполнения запроса (созд. организации)");
                                            }
                                        });
                            }

                            @Override
                            public void onFailure(@NotNull Call<DistrictResponse> call, @NotNull Throwable t) {
                                //ошибка во время выполнения запроса
                            }

                        });
            }
        });
/*
        registerOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                orgName = name.getText().toString();
                orgDescription = description.getText().toString();
                orgEmail = email.getText().toString();
                orgPhone = number.getText().toString();


                final RegisteredOrganization registeredOrganization = new RegisteredOrganization(Address address, orgName, orgDescription, orgEmail, orgPhone);
                //if (checkDataEntered()){
                ApplicationService.getInstance()
                        .getJSONApi()
                        .postRegistedOrgData(registeredOrganization)
                        .enqueue(new Callback<RegisteredOrganization>() {
                            @Override
                            public void onResponse(@NonNull Call<RegisteredOrganization> call, @NonNull Response<RegisteredOrganization> response) {
                                if (response.isSuccessful()){
                                    //запрос выполнился успешно
                                    showMessage("Регистрация вполнена успешно");
                                }
                                else {
                                    //сервер вернул ошибку
                                    showMessage("Регистрация не вполнена");
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<RegisteredOrganization> call, @NonNull Throwable t) {
                                showMessage("Ошибка во время выполнения запроса");
                            }
                        });
                //}
            }
        });
 */
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

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void showMessage(String string){
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }
}

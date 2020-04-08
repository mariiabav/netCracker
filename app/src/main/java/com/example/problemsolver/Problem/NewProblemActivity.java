package com.example.problemsolver.Problem;

import androidx.annotation.NonNull;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.Map.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.Map.Models.DistrictResponse.FeatureMember;
import com.example.problemsolver.MapService;
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
import com.yandex.mapkit.search.SearchManager;
import com.yandex.runtime.image.ImageProvider;

import java.util.List;

import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProblemActivity extends Activity /*implements SuggestSession.SuggestListener*/ {

    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";
    private final int RESULT_NUMBER_LIMIT = 5;


    private SearchManager searchManager;
    /*
    private SuggestSession suggestSession;
    private ArrayAdapter resultAdapter;
    private List<String> suggestResult;
    private ListView suggestResultView;
     */

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

    TextView layoutAddress;

    private ApplicationService applicationService;
    private MapService mapService;

    private final String API_KEY = "7e3eee55-cf92-4361-919e-e1666d3df1d1";

    /*
    private final Point CENTER = new Point(55.75, 37.62);
    private final BoundingBox BOUNDING_BOX = new BoundingBox(
            new Point(60.092945, 29.961734),
            new Point(59.705141, 30.787196));
    private final SuggestOptions SEARCH_OPTIONS = new SuggestOptions().setSuggestTypes(
           SuggestType.GEO.value);

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        //SearchFactory.initialize(this);
        setContentView(R.layout.activity_new_problem);

        mapService = MapService.getInstance();

        //searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        //suggestSession = searchManager.createSuggestSession();

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
        /*
        final EditText queryEdit = findViewById(R.id.suggest_query);
        suggestResultView = findViewById(R.id.suggest_result);
         */
        problem = findViewById(R.id.btn_problem);
        type = findViewById(R.id.problem_input_name);
        description = findViewById(R.id.problem_input_description);
        mapView = findViewById(R.id.map);
        layoutAddress = findViewById(R.id.query_address);


        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("нажалась");
                problemType = type.getText().toString();
                problemDescription = description.getText().toString();
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
                                NewProblem newProblem = new NewProblem(fullAddress, problemType, problemDescription, "created", 0, coordinates);

                                ApplicationService.getInstance()
                                        .getJSONApi()
                                        .postNewProblemData(newProblem)
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
            }
        });





        /*
        suggestResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String strSelectedFeature = (String) parent.getAdapter().getItem(position);
                queryEdit.setText(strSelectedFeature);
                address = strSelectedFeature;
                suggestResult.clear();
                problemService
                        .getJSONApi()
                        .getRegionData(API_key, format, address)
                        .enqueue(new Callback<RegionDataResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<RegionDataResponse> call, Response<RegionDataResponse> response) {
                                coordinates = response.body()
                                        .getResponse()
                                        .getGeoObjectCollection()
                                        .getFeatureMember()
                                        .get(0)
                                        .getGeoObject()
                                        .getPoint()
                                        .getPos();
                            }

                            @Override
                            public void onFailure(@NonNull Call<RegionDataResponse> call, @NonNull Throwable t) {
                            }
                        });
            }
        });
         */

        /*
        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                suggestResult);
        suggestResultView.setAdapter(resultAdapter);

        queryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                requestSuggest(editable.toString());
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

    /*
    @Override
    public void onResponse(@NonNull List<SuggestItem> suggest) {
        suggestResult.clear();
        for (int i = 0; i < Math.min(RESULT_NUMBER_LIMIT, suggest.size()); i++) {
            suggestResult.add(suggest.get(i).getDisplayText());
        }
        resultAdapter.notifyDataSetChanged();
        suggestResultView.setVisibility(View.VISIBLE);
    }




    @Override
    public void onError(@NonNull Error error) {
    }


    private void requestSuggest(String query) {
        suggestResultView.setVisibility(View.INVISIBLE);
        suggestSession.suggest(query, BOUNDING_BOX, SEARCH_OPTIONS, this);
    }
     */

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

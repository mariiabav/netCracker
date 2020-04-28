package com.example.problemsolver.map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.problem.model.NewProblem;
import com.example.problemsolver.R;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends Activity implements UserLocationObjectListener, CameraListener {

    private MapView mapView;
    private UserLocationLayer userLocationLayer;
    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";
    private ArrayList<NewProblem> problems;
    private SharedPreferences settings;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);

        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT","");

        setContentView(R.layout.activity_map);
        super.onCreate(savedInstanceState);
        mapView = findViewById(R.id.mapview);

        mapView.getMap().setRotateGesturesEnabled(true);
        mapView.getMap().move(new CameraPosition(new Point(0, 0), 14, 0, 0));

        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);

        getProblems();

    }

    @Override
    protected void onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float) (mapView.getWidth() * 0.5), (float) (mapView.getHeight() * 0.5)),
                new PointF((float) (mapView.getWidth() * 0.5), (float) (mapView.getHeight() * 0.83)));
        // При определении направления движения устанавливается следующая иконка
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow)); //ТУТ КАРТИНКА ДРУГАЯ
        // При получении координат местоположения устанавливается следующая иконка
        userLocationView.getPin().setIcon(ImageProvider.fromResource(
                this, R.drawable.user_arrow)); //ТУТ КАРТИНКА ДРУГАЯ
        userLocationView.getAccuracyCircle().setFillColor(Color.rgb(236, 112, 99 ));
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }

    public void getProblems() {
        ApplicationService.getInstance()
                .getJSONApi()
                .getAllProblems(token)
                .enqueue(new Callback<List<NewProblem>>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(@NonNull Call<List<NewProblem>> call, @NonNull Response<List<NewProblem>> response) {
                        problems = (ArrayList<NewProblem>) response.body();
                        for (NewProblem problem : problems) {
                            String[] array = problem.getCoordinates().split(",");
                            Point point = new Point(Double.parseDouble(array[1]), Double.parseDouble(array[0]));
                            MapObject mapObject = mapView.getMap().getMapObjects().addPlacemark(point, ImageProvider.fromResource(getApplicationContext(), R.drawable.red_geo_point));
                            String info = problem.getProblemName() + " " + problem.getDescription() + problem.getAddress().getStreet() + problem.getAddress().getBuilding();
                            mapObject.setUserData(info);
                            mapObject.addTapListener(new MapObjectTapListener() {
                                @Override
                                public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                                    showMessage((String)mapObject.getUserData());
                                    return true;
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<NewProblem>> call, @NonNull Throwable t) {

                    }
                });
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateSource cameraUpdateSource, boolean b) {
        userLocationLayer.resetAnchor();
    }
}

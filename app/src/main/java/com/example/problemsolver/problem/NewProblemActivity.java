package com.example.problemsolver.problem;

import androidx.annotation.NonNull;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.problemsolver.ApplicationService;
import com.example.problemsolver.ServerApi;
import com.example.problemsolver.map.Models.DistrictResponse.DistrictResponse;
import com.example.problemsolver.map.Models.DistrictResponse.FeatureMember;
import com.example.problemsolver.Photo;
import com.example.problemsolver.organization.model.FeedOrgResponse;
import com.example.problemsolver.organization.model.RegisteredOrganization;
import com.example.problemsolver.problem.model.Address;
import com.example.problemsolver.problem.model.Area;
import com.example.problemsolver.problem.model.DBFile;
import com.example.problemsolver.problem.model.NewProblem;
import com.example.problemsolver.problem.model.Owner;
import com.example.problemsolver.R;
import com.example.problemsolver.utils.PaginationAdapterCallback;
import com.example.problemsolver.utils.PaginationScrollListener;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProblemActivity extends Activity implements PaginationAdapterCallback {


    private ImageView imageView;
    private ImageButton pickImage;
    private final int Pick_image = 1;
    private Bitmap selectedImage;

    private final String MAPKIT_API_KEY = "d57819df-534a-4ba4-89d4-430e73a03ab3";

    private MapView mapView;
    private LocationManager locationManager;
    private double myLatitude;
    private double myLongitude;
    private Context mContext;

    private String format = "json";
    private Point myLocation;
    private PlacemarkMapObject placemarkMapObject;

    private String[] splitedAddress;
    private String street;
    private String building;

    private Button problem, chooseOrgBtn;
    private EditText type, description;
    private String problemType, problemDescription;

    private String address;
    private String coordinates;
    private String adminAreaName;

    private String token;
    private String personId;
    private SharedPreferences settings;
    private String pictureId;

    private TextView layoutAddress;

    private MapService mapService;

    private final String API_KEY = "7e3eee55-cf92-4361-919e-e1666d3df1d1";

    private static final int PAGE_START = 0;

    private OrgPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private int currentPage = PAGE_START;

    private String sortBy = "rate";
    private ServerApi serverApi;
    private String orgId;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_new_problem);
        settings = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        token = settings.getString("JWT", "");
        personId = settings.getString("id","");
        mContext = this;
        serverApi = ApplicationService.getInstance().getJSONApi();

        swipeRefreshLayout = findViewById(R.id.main_swiperefresh);
        swipeRefreshLayout.setVisibility(View.GONE);

        requestMultiplePermissions();
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mapService = MapService.getInstance();

        locationManager = MapKitFactory.getInstance().createLocationManager();

        problem = findViewById(R.id.btn_problem);
        type = findViewById(R.id.editText_new_problem_name);
        description = findViewById(R.id.editText_new_problem_description);
        mapView = findViewById(R.id.map);
        layoutAddress = findViewById(R.id.editText_new_problem_address);

        imageView = findViewById(R.id.imageView);
        pickImage = findViewById(R.id.problem_photo_btn);

        chooseOrgBtn = findViewById(R.id.choose_org);
        chooseOrgBtn.setClickable(false);
        chooseOrgBtn.setBackgroundColor(getResources().getColor(R.color.grey));

        chooseOrgBtn.setOnClickListener(view -> {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            loadFirstPage();
        });

        locationUpdate();

        pickImage.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
        });

        problem.setOnClickListener(view -> {
            problemType = type.getText().toString();
            problemDescription = description.getText().toString();
            createNewProblem();
        });


        ScrollView mainScrollView =  findViewById(R.id.main_scroll_view);

        ImageView transparentImageView = findViewById(R.id.transparent_image);
        transparentImageView.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:

                case MotionEvent.ACTION_MOVE:
                    mainScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                case MotionEvent.ACTION_UP:
                    mainScrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                default:
                    return true;
            }
        });


        adapter = new OrgPaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView rv = findViewById(R.id.main_recycler);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);

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
                                    pictureId = response.body().getId();

                                }
                                else {

                                }
                                problem.setClickable(true);
                            }
                            @Override
                            public void onFailure(@NonNull Call<Photo> call, @NonNull Throwable t) {

                            }
                        });
            }
        }
    }

    private void showMessage(String string) {
        Toast t = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        t.show();
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
                        problem.setClickable(false);
                        uploadFile(imageUri);
                    }
                    try {
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        imageView.setImageResource(R.drawable.status_pic_unsolved); //каринка "невозмонжо отобразить"
                    }
                }
        }
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
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

    //запрос на получение адреса
    private void addressRequest() {
        mapService.getJSONApi()
                .getDistrictName(token, API_KEY, format, coordinates)
                .enqueue(new Callback<DistrictResponse>() {

                    @Override
                    public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                        address = response.body().getResponse()
                                .getGeoObjectCollection()
                                .getFeatureMember()
                                .get(0)
                                .getGeoObject()
                                .getMetaDataProperty()
                                .getGeocoderMetaData()
                                .getText();

                        layoutAddress.setText(address);

                        chooseOrgBtn.setClickable(true);
                        chooseOrgBtn.setBackgroundColor(getResources().getColor(R.color.red));

                        adminAreaRequest();
                    }

                    @Override
                    public void onFailure(Call<DistrictResponse> call, Throwable t) {

                    }
                });
    }

    //запрос на получение района
    private void adminAreaRequest() {
        mapService
                .getJSONApi()
                .getDistrictName(token, API_KEY, format, coordinates)
                .enqueue(new Callback<DistrictResponse>() {
                    @Override
                    public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
                        List<FeatureMember> featureMembers = response.body().getResponse().getGeoObjectCollection()
                                .getFeatureMember();
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
                                if (adminAreaName.split(" ").length >= 2 && adminAreaName.split(" ")[1].equals("район")) {
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DistrictResponse> call, Throwable t) {

                    }
                });
    }

    //запрос регистрации проблемы
    public void createNewProblem() {
        mapService
                .getJSONApi()
                .getDistrictName(token, API_KEY, format, coordinates)
                .enqueue(new Callback<DistrictResponse>() {
                    @Override
                    public void onResponse(Call<DistrictResponse> call, Response<DistrictResponse> response) {
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
                        DBFile dbFile = new DBFile(pictureId);
                        RegisteredOrganization org = new RegisteredOrganization(UUID.fromString(orgId));
                        NewProblem newProblem = new NewProblem(fullAddress, problemType, problemDescription, "init",  coordinates, owner, dbFile, org);

                        ApplicationService.getInstance()
                                .getJSONApi()
                                .postNewProblemData(token, newProblem)
                                .enqueue(new Callback<NewProblem>() {
                                    @Override
                                    public void onResponse(@NonNull Call<NewProblem> call, @NonNull Response<NewProblem> response) {
                                        if (response.isSuccessful()) {
                                            showMessage("Проблема отправлена успешно.");
                                        } else {
                                            showMessage("Сервен вернул ошибку. Проблема не отправлена.");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<NewProblem> call, @NonNull Throwable t) {
                                        showMessage("Ошибка во время выполнения запроса. Проблема не отправлена.");
                                    }
                                });
                    }

                    @Override
                    public void onFailure(@NotNull Call<DistrictResponse> call, @NotNull Throwable t) {

                    }
                });
    }

    public void locationUpdate() {
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
                        addressRequest();
                    }
                });
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_org, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_org_refresh:
                swipeRefreshLayout.setRefreshing(true);
                doRefresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doRefresh() {
        if (callServerApi().isExecuted())
            callServerApi().cancel();

        adapter.getOrgs().clear();
        adapter.notifyDataSetChanged();
        currentPage = 0;
        isLastPage = false;
        loadFirstPage();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadFirstPage() {

        currentPage = PAGE_START;

        callServerApi().enqueue(new Callback<FeedOrgResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedOrgResponse> call, @NotNull Response<FeedOrgResponse> response) {

                List<RegisteredOrganization> results = fetchResults(response);
                adapter.addAll(results);
                adapter.addSortBy(sortBy);
                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedOrgResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private List<RegisteredOrganization> fetchResults(Response<FeedOrgResponse> response) {
        return response.body().getOrganizationList();
    }

    private void loadNextPage() {

        callServerApi().enqueue(new Callback<FeedOrgResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeedOrgResponse> call, @NotNull Response<FeedOrgResponse> response) {

                adapter.removeLoadingFooter();
                isLoading = false;

                List<RegisteredOrganization> results = fetchResults(response);
                adapter.addAll(results);
                if(results.size() == 0) {
                    isLastPage = true;
                }
                else {
                    adapter.addLoadingFooter();
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeedOrgResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private Call<FeedOrgResponse> callServerApi() {
        return serverApi.getAreaOrgs(
                token,
                currentPage,
                8,
                sortBy,
                "desc",
                adminAreaName
        );
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }


    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void onChooseClick(View view) {
        orgId = (String)view.getTag();
        showMessage(orgId);
        swipeRefreshLayout.setVisibility(View.GONE);
    }
}

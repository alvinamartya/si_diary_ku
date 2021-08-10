package id.ac.astra.polman.sidiaryku.ui.activity.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng defaultLocation = new LatLng(-6.127450, 106.873710);

    private GoogleMap map;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private Place place = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        binding.setViewModel(new MapsViewModel());
        setContentView(binding.getRoot());

        // auto complete place fragment
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);
        Objects.requireNonNull(autocompleteSupportFragment).setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull @NotNull Place place) {
                changeMap(place.getLatLng(), place.getName(), place.getAddress());
            }

            @Override
            public void onError(@NonNull @NotNull Status status) {
                Log.e(TAG, "onError: " + status.getStatusMessage());
            }
        });

        // map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Objects.requireNonNull(mapFragment).getMapAsync(this);
        Places.initialize(this, getString(R.string.google_maps_key));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // pick location
        binding.pickLocationMapsButton.setOnClickListener(v -> {
            binding.getViewModel().setPlaceLiveData(place);
            onBackPressed();
        });
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        map = googleMap;
        pickCurrentLocation();
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    // get device location
    private void getDeviceLocation() {
        try {
            if (!isLocationGranted()) {
                return;
            }

            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult
                    .addOnCompleteListener(this, task -> {
                        LatLng latLng;
                        String placeName = "";
                        String placeSnippet = "";
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            placeName = getString(R.string.your_current_location);
                            placeSnippet = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                            Log.d(TAG, placeSnippet);
                            latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                            binding
                                    .getViewModel()
                                    .getGeoLocation(latLng, getString(R.string.google_maps_key))
                                    .observe(this, place -> {
                                        if (place != null) {
                                            changeMap(place.getLatLng(), place.getName(), place.getAddress());
                                        }
                                    });
                        } else {
                            placeName = getString(R.string.default_info_title);
                            placeSnippet = getString(R.string.default_info_title);
                            Log.e(TAG, "getDeviceLocation: " + task.getException());
                            latLng = defaultLocation;
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        }

                        changeMap(latLng, placeName, placeSnippet);
                    });
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: " + e.getLocalizedMessage());
        }
    }

    // get current location, if is not has permission then select default location
    private void pickCurrentLocation() {
        if (map == null) return;

        if (isLocationGranted()) {
            getDeviceLocation();
        } else {
            Log.i(TAG, "The user did not grant location permission.");
            changeMap(defaultLocation, getString(R.string.default_info_title), getString(R.string.default_info_snippet));
            getLocationPermission();
        }
    }

    // location permission
    private void getLocationPermission() {
        if (!isLocationGranted()) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // check permission
    private boolean isLocationGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // change map
    private void changeMap(LatLng latLng, String title, String snippet) {
        map.clear();
        map.addMarker(new MarkerOptions()
                .title(title)
                .position(latLng)
                .snippet(snippet));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

        // set Place
        place = Place.builder()
                .setName(title)
                .setAddress(snippet)
                .setLatLng(latLng)
                .build();
    }
}
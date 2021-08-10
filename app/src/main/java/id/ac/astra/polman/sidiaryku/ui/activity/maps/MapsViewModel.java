package id.ac.astra.polman.sidiaryku.ui.activity.maps;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import id.ac.astra.polman.sidiaryku.api.BaseApi;
import id.ac.astra.polman.sidiaryku.api.GeoService;
import id.ac.astra.polman.sidiaryku.dao.PlaceDao;
import id.ac.astra.polman.sidiaryku.model.AddressComponentModel;
import id.ac.astra.polman.sidiaryku.model.GeoCodeModel;
import id.ac.astra.polman.sidiaryku.model.ResultAddressModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsViewModel extends ViewModel {
    private static final String TAG = MapsViewModel.class.getSimpleName();
    private final GeoService geoService;

    public MapsViewModel() {
        geoService = BaseApi.getGeoService();
    }

    public void setPlaceLiveData(Place place) {
        PlaceDao.initialize();
        PlaceDao.setPlaceLiveData(place);
    }

    public LiveData<Place> getGeoLocation(LatLng latLng, String apiKey) {
        MutableLiveData<Place> placeMutableLiveData = new MutableLiveData<>();
        String latLong = latLng.latitude + "," + latLng.longitude;

        Call<GeoCodeModel> geoCodeModelCall = geoService.getUserAddress(latLong, apiKey);
        geoCodeModelCall.enqueue(new Callback<GeoCodeModel>() {
            @Override
            public void onResponse(@NotNull Call<GeoCodeModel> call, @NotNull Response<GeoCodeModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResultAddressModel> resultAddressModelList = response.body().getResults();
                    if (resultAddressModelList.size() > 0) {
                        ResultAddressModel resultAddressModel = resultAddressModelList.get(0);
                        List<AddressComponentModel> addressComponentModels = resultAddressModel.getAddress_components();

                        AddressComponentModel administrativeLevel2 = addressComponentModels
                                .stream()
                                .filter(x -> x.getTypes().contains("administrative_area_level_2"))
                                .findFirst()
                                .orElse(null);

                        AddressComponentModel administrativeLevel4 = addressComponentModels
                                .stream()
                                .filter(x -> x.getTypes().contains("administrative_area_level_4"))
                                .findFirst()
                                .orElse(null);

                        if (administrativeLevel2 != null && administrativeLevel4 != null) {
                            Place place = Place.builder()
                                    .setAddress(administrativeLevel4.getLong_name() + ", " + administrativeLevel2.getLong_name())
                                    .setName("Current location")
                                    .setLatLng(latLng)
                                    .build();

                            placeMutableLiveData.setValue(place);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<GeoCodeModel> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return placeMutableLiveData;
    }
}

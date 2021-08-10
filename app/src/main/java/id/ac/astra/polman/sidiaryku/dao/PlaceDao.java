package id.ac.astra.polman.sidiaryku.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.model.Place;

public class PlaceDao {
    private static PlaceDao INSTANCE;
    private static final MutableLiveData<Place> placeMutableLiveData = new MutableLiveData<>();

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new PlaceDao();
        }
    }

    public static void setPlaceLiveData(Place place) {
        placeMutableLiveData.postValue(place);
    }

    public static LiveData<Place> getPlaceLiveData() {
        return placeMutableLiveData;
    }
}

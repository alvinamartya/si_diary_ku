package id.ac.astra.polman.sidiaryku.api;

import id.ac.astra.polman.sidiaryku.model.GeoCodeModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoService {
    @GET("geocode/json")
    Call<GeoCodeModel> getUserAddress(@Query("latlng") String latLong, @Query("key") String key);
}

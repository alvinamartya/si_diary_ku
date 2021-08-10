package id.ac.astra.polman.sidiaryku.api;

public class BaseApi {
    private final static String API_URL = "https://maps.googleapis.com/maps/api/";
    public static GeoService getGeoService() {
        return RetrofitClient.getClient(API_URL).create(GeoService.class);
    }
}

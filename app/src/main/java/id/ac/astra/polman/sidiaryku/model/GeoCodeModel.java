package id.ac.astra.polman.sidiaryku.model;

import java.util.List;

public class GeoCodeModel {
    private List<ResultAddressModel> results;

    public GeoCodeModel(List<ResultAddressModel> results) {
        this.results = results;
    }

    public GeoCodeModel() {
    }

    public List<ResultAddressModel> getResults() {
        return results;
    }

    public void setResults(List<ResultAddressModel> results) {
        this.results = results;
    }
}

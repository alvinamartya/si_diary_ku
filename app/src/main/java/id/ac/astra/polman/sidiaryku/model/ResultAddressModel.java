package id.ac.astra.polman.sidiaryku.model;

import java.util.List;

public class ResultAddressModel {
    private List<AddressComponentModel> address_components;
    private String formatted_address;

    public ResultAddressModel(List<AddressComponentModel> address_components, String formatted_address) {
        this.address_components = address_components;
        this.formatted_address = formatted_address;
    }

    public ResultAddressModel() {
    }

    public List<AddressComponentModel> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<AddressComponentModel> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
}

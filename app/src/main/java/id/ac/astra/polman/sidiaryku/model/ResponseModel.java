package id.ac.astra.polman.sidiaryku.model;

public class ResponseModel {
    private boolean isSuccess;
    private String message;

    public ResponseModel(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public ResponseModel() {
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

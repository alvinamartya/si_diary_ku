package id.ac.astra.polman.sidiaryku.model;

public class AccountModel {
    private String name;
    private String note;

    public AccountModel(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public AccountModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

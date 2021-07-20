package id.ac.astra.polman.sidiaryku.entity;

public class UserEntity {
    private String email;
    private String password;
    private String name;
    private String note;

    public UserEntity(String email, String password, String name, String note) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.note = note;
    }

    public  UserEntity() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

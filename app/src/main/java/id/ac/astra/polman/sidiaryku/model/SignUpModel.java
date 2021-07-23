package id.ac.astra.polman.sidiaryku.model;

public class SignUpModel {
    private String email;
    private String name;
    private String password;
    private String repeatPassword;

    public SignUpModel(String email, String name, String password, String repeatPassword) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public SignUpModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}

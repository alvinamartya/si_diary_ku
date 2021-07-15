package id.ac.astra.polman.sidiaryku.ui.activity.login;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.LoginModel;
import id.ac.astra.polman.sidiaryku.model.ResponseModel;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAuthHelper;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class LoginViewModel extends ViewModel {
    public LiveData<ResponseModel> login(Activity activity, LoginModel loginModel) {
        MutableLiveData<ResponseModel> loginLiveData = new MutableLiveData<>();

        if (loginModel.getEmail().isEmpty()) {
            loginLiveData.postValue(new ResponseModel(false, "Email is empty"));
        } else if (loginModel.getPassword().isEmpty()) {
            loginLiveData.postValue(new ResponseModel(false, "Password is empty"));
        } else {
            FirebaseAuthHelper
                    .signIn(activity, loginModel.getEmail(), loginModel.getPassword())
                    .observe((LifecycleOwner) activity, responseModel -> {
                        if (responseModel.isSuccess()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db
                                    .collection("users")
                                    .document(loginModel.getEmail())
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snapshot = task.getResult();
                                            new Preference(activity).setUser(new UserEntity(
                                                    loginModel.getEmail(),
                                                    loginModel.getPassword(),
                                                    snapshot != null ? snapshot.getString("name") : ""
                                            ));

                                            loginLiveData.postValue(new ResponseModel(true, "Login is success"));
                                        } else {
                                            loginLiveData.postValue(new ResponseModel(false, Objects.requireNonNull(task.getException()).getLocalizedMessage()));
                                        }
                                    });
                        } else {
                            loginLiveData.postValue(new ResponseModel(false, responseModel.getMessage()));
                        }
                    });
        }

        return loginLiveData;
    }
}

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
import id.ac.astra.polman.sidiaryku.utils.FirebaseAnalyticsHelper;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAuthHelper;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class LoginViewModel extends ViewModel {
    private final static String TAG = LoginViewModel.class.getSimpleName();

    public LiveData<ResponseModel> login(Activity activity, LoginModel loginModel) {
        MutableLiveData<ResponseModel> loginLiveData = new MutableLiveData<>();

        // instance firebase auth
        FirebaseAuthHelper.getInstance();

        // firebase analytics log user
        FirebaseAnalyticsHelper analytics = new FirebaseAnalyticsHelper(activity);
        analytics.logEventUserLogin(loginModel.getEmail());

        // validate data
        if (loginModel.getEmail().isEmpty()) {
            loginLiveData.postValue(new ResponseModel(false, "Email is empty"));
        } else if (loginModel.getPassword().isEmpty()) {
            loginLiveData.postValue(new ResponseModel(false, "Password is empty"));
        } else {
            // sign in
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
                                                    snapshot != null ? snapshot.getString("name") : "",
                                                    snapshot != null ? snapshot.getString("note") : ""
                                            ));

                                            loginLiveData.postValue(new ResponseModel(true, "Login is success"));
                                        } else {
                                            String errMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                                            loginLiveData.postValue(new ResponseModel(false, errMessage));
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

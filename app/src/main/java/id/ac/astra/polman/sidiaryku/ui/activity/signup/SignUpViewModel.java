package id.ac.astra.polman.sidiaryku.ui.activity.signup;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.ResponseModel;
import id.ac.astra.polman.sidiaryku.model.SignUpModel;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAnalyticsHelper;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAuthHelper;
import id.ac.astra.polman.sidiaryku.utils.Preference;
import id.ac.astra.polman.sidiaryku.utils.Validation;

public class SignUpViewModel extends ViewModel {
    private final static String TAG = SignUpViewModel.class.getSimpleName();

    public LiveData<ResponseModel> signUp(Activity activity, SignUpModel signUpModel) {
        MutableLiveData<ResponseModel> signUpLiveData = new MutableLiveData<>();

        // instance firebase auth
        FirebaseAuthHelper.getInstance();

        // firebase analytics log user
        FirebaseAnalyticsHelper analytics = new FirebaseAnalyticsHelper(activity);
        analytics.logEventUserLogin(signUpModel.getEmail());

        // validate data
        if (signUpModel.getEmail().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, activity.getString(R.string.email_is_empty)));
        } else if (signUpModel.getPassword().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, activity.getString(R.string.password_is_empty)));
        } else if (signUpModel.getRepeatPassword().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, activity.getString(R.string.repeat_password_is_empty)));
        } else if (signUpModel.getName().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, activity.getString(R.string.name_is_empty)));
        } else if (!Validation.matchEmail(signUpModel.getEmail())) {
            signUpLiveData.postValue(new ResponseModel(false, activity.getString(R.string.invalid_email)));
        } else if (!signUpModel.getPassword().equals(signUpModel.getRepeatPassword())) {
            signUpLiveData.postValue(new ResponseModel(false, activity.getString(R.string.password_and_repeat_password_is_not_same)));
        } else if (signUpModel.getPassword().length() < 6) {
            signUpLiveData.postValue(new ResponseModel(false, activity.getString(R.string.password_at_least_has_six_character)));
        } else {
            // sign up
            FirebaseAuthHelper
                    .signUp(activity, signUpModel.getEmail(), signUpModel.getPassword())
                    .observe((LifecycleOwner) activity, responseModel -> {
                        if (responseModel.isSuccess()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> user = new HashMap<>();
                            user.put("email", signUpModel.getEmail());
                            user.put("name", signUpModel.getName());
                            user.put("note", "");

                            // insert user to FireStore on collection user
                            db
                                    .collection("users")
                                    .document(signUpModel.getEmail())
                                    .set(user)
                                    .addOnSuccessListener(doc -> {
                                        new Preference(activity).setUser(new UserEntity(
                                                signUpModel.getEmail(),
                                                signUpModel.getPassword(),
                                                signUpModel.getName(),
                                                ""
                                        ));

                                        analytics.logUser(signUpModel.getEmail());
                                        signUpLiveData.postValue(new ResponseModel(true, activity.getString(R.string.sign_up_account_is_success)));
                                    })
                                    .addOnFailureListener(e -> {
                                        signUpLiveData.postValue(new ResponseModel(false, e.getLocalizedMessage()));
                                        Log.e(TAG, "signUp: " + e.getLocalizedMessage());
                                    });
                        } else {
                            signUpLiveData.postValue(responseModel);
                        }
                    });
        }

        return signUpLiveData;
    }
}

package id.ac.astra.polman.sidiaryku.ui.activity.signup;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.ac.astra.polman.sidiaryku.model.SignUpModel;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.ResponseModel;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAuthHelper;
import id.ac.astra.polman.sidiaryku.utils.Preference;
import id.ac.astra.polman.sidiaryku.utils.Validation;

public class SignUpViewModel extends ViewModel {
    public LiveData<ResponseModel> signUp(Activity activity, SignUpModel signUpModel) {
        MutableLiveData<ResponseModel> signUpLiveData = new MutableLiveData<>();

        if (signUpModel.getEmail().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, "Email is empty"));
        } else if (signUpModel.getPassword().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, "Password is empty"));
        } else if (signUpModel.getRepeatPassword().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, "Repeat password is empty"));
        } else if (signUpModel.getName().isEmpty()) {
            signUpLiveData.postValue(new ResponseModel(false, "Name is empty"));
        } else if (!Validation.matchEmail(signUpModel.getEmail())) {
            signUpLiveData.postValue(new ResponseModel(false, "Invalid email"));
        } else if (!signUpModel.getPassword().equals(signUpModel.getRepeatPassword())) {
            signUpLiveData.postValue(new ResponseModel(false, "Password and Repeat Password are not same"));
        } else {
            FirebaseAuthHelper.signUp(activity, signUpModel.getEmail(), signUpModel.getPassword()).observe((LifecycleOwner) activity, responseModel -> {
                if (responseModel.isSuccess()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> user = new HashMap<>();
                    user.put("email", signUpModel.getEmail());
                    user.put("name", signUpModel.getName());

                    // insert user to FireStore on collection user
                    db
                            .collection("users")
                            .document(signUpModel.getEmail())
                            .set(user)
                            .addOnSuccessListener(doc -> {
                                new Preference(activity).setUser(new UserEntity(
                                        signUpModel.getEmail(),
                                        signUpModel.getPassword(),
                                        signUpModel.getName()
                                ));

                                signUpLiveData.postValue(new ResponseModel(true, "Success"));
                            })
                            .addOnFailureListener(e -> {
                                signUpLiveData.postValue(new ResponseModel(false, e.getLocalizedMessage()));
                            });
                } else {
                    signUpLiveData.postValue(responseModel);
                }
            });
        }

        return signUpLiveData;
    }
}

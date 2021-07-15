package id.ac.astra.polman.sidiaryku.utils;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.model.ResponseModel;

public class FirebaseAuthHelper {
    private static FirebaseAuth auth;

    public static void getInstance() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
    }

    public static FirebaseUser getFirebaseUser() {
        if (auth != null && auth.getCurrentUser() != null) {
            return auth.getCurrentUser();
        }

        return null;
    }

    public static LiveData<ResponseModel> signUp(Activity activity, String email, String password) {
        MutableLiveData<ResponseModel> authLiveData = new MutableLiveData<>();

        // sign up account to firebase
        auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        authLiveData.postValue(new ResponseModel(true, "Sign up is success"));
                    } else {
                        authLiveData.postValue(new ResponseModel(false, Objects.requireNonNull(task.getException()).getLocalizedMessage()));
                    }
                });

        return authLiveData;
    }

    public static LiveData<ResponseModel> signIn(Activity activity, String email, String password) {
        MutableLiveData<ResponseModel> authLiveData = new MutableLiveData<>();

        // login to firebase
        auth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        authLiveData.postValue(new ResponseModel(true, "Success"));
                    } else {
                        authLiveData.postValue(new ResponseModel(false, Objects.requireNonNull(task.getException()).getLocalizedMessage()));
                    }
                });

        return authLiveData;
    }

    public static void signOut() {
        if (auth != null) {
            auth.signOut();
        }
    }
}

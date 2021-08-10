package id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.change_password;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.ChangePasswordModel;
import id.ac.astra.polman.sidiaryku.model.ResponseModel;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAuthHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class ChangePasswordViewModel extends ViewModel {
    private final String TAG = ChangePasswordViewModel.class.getSimpleName();

    public LiveData<ResponseModel> changePassword(Context context, ChangePasswordModel changePasswordModel) {
        MutableLiveData<ResponseModel> changePasswordLiveData = new MutableLiveData<>();

        PreferenceHelper preferenceHelper = new PreferenceHelper(context);
        UserEntity userEntity = preferenceHelper.getUser();

        if (changePasswordModel.getOldPassword().isEmpty()) {
            changePasswordLiveData.postValue(new ResponseModel(false, context.getString(R.string.old_password_is_empty)));
        } else if (changePasswordModel.getNewPassword().isEmpty()) {
            changePasswordLiveData.postValue(new ResponseModel(false, context.getString(R.string.new_password_is_empty)));
        } else if (changePasswordModel.getRepeatPassword().isEmpty()) {
            changePasswordLiveData.postValue(new ResponseModel(false, context.getString(R.string.repeat_password_is_empty)));
        } else if (!changePasswordModel.getOldPassword().equals(userEntity.getPassword())) {
            changePasswordLiveData.postValue(new ResponseModel(false, context.getString(R.string.invalid_old_password)));
        } else if (!changePasswordModel.getNewPassword().equals(changePasswordModel.getRepeatPassword())) {
            changePasswordLiveData.postValue(new ResponseModel(false, context.getString(R.string.new_password_and_repeat_password_is_not_same)));
        } else if (changePasswordModel.getNewPassword().length() < 6) {
            changePasswordLiveData.postValue(new ResponseModel(false, context.getString(R.string.new_password_at_least_has_six_character)));
        } else {
            AuthCredential credential = EmailAuthProvider.getCredential(userEntity.getEmail(), userEntity.getPassword());

            FirebaseAuthHelper.getInstance();
            FirebaseUser firebaseUser = FirebaseAuthHelper.getFirebaseUser();
            if (firebaseUser != null) {
                firebaseUser
                        .reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                firebaseUser
                                        .updatePassword(changePasswordModel.getNewPassword())
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                changePasswordLiveData.postValue(new ResponseModel(true, context.getString(R.string.change_password_is_success)));
                                            } else {
                                                String errMessage = Objects.requireNonNull(task2.getException()).getLocalizedMessage();
                                                changePasswordLiveData.postValue(new ResponseModel(false, errMessage));
                                                Log.e(TAG, "changePassword: " + errMessage);
                                            }
                                        })
                                        .addOnFailureListener(task2 -> {
                                            changePasswordLiveData.postValue(new ResponseModel(false, task2.getLocalizedMessage()));
                                            Log.e(TAG, "changePassword: " + task2.getLocalizedMessage());
                                        });
                            } else {
                                String errMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                                changePasswordLiveData.postValue(new ResponseModel(false, errMessage));
                                Log.e(TAG, "changePassword: " + errMessage);
                            }
                        })
                        .addOnFailureListener(task -> {
                            changePasswordLiveData.postValue(new ResponseModel(false, task.getLocalizedMessage()));
                            Log.e(TAG, "changePassword: " + task.getLocalizedMessage());
                        });
            }
        }

        return changePasswordLiveData;
    }
}
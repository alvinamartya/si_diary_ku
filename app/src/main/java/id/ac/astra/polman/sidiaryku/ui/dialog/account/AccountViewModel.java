package id.ac.astra.polman.sidiaryku.ui.dialog.account;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.AccountModel;
import id.ac.astra.polman.sidiaryku.model.ResponseModel;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class AccountViewModel extends ViewModel {
    private final String TAG = AccountViewModel.class.getSimpleName();
    public LiveData<ResponseModel> changeNameAndNote(Context context, AccountModel accountModel) {
        MutableLiveData<ResponseModel> accountLiveData = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Preference preference = new Preference(context);
        UserEntity userEntity = preference.getUser();

        if(accountModel.getName().isEmpty()) {
            accountLiveData.postValue(new ResponseModel(false, "Name is empty"));
        } else {
            Map<String, Object> account = new HashMap<>();
            account.put("name", accountModel.getName());
            account.put("note", accountModel.getNote());
            account.put("email", userEntity.getEmail());

            db
                    .collection("users")
                    .document(userEntity.getEmail())
                    .set(account)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            String successMessage = context.getString(R.string.message_success_account);
                            accountLiveData.postValue(new ResponseModel(true, successMessage));
                        } else {
                            String errMessage = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                            accountLiveData.postValue(new ResponseModel(false, errMessage));
                            Log.e(TAG, "changeNameAndNote: " + errMessage);
                        }
                    })
                    .addOnFailureListener(task -> {
                        accountLiveData.postValue(new ResponseModel(false, task.getLocalizedMessage()));
                        Log.e(TAG, "changeNameAndNote: " + task.getLocalizedMessage() );
                    });
        }

        return accountLiveData;
    }
}
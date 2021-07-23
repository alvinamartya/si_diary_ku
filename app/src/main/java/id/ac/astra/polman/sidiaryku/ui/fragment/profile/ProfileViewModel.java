package id.ac.astra.polman.sidiaryku.ui.fragment.profile;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAuthHelper;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class ProfileViewModel extends ViewModel {
    private static final MutableLiveData<UserEntity> userViewModel = new MutableLiveData<>();

    public LiveData<UserEntity> getUser() {
        return userViewModel;
    }

    public void loadUser(Context context) {
        Preference preference = new Preference(context);
        System.out.println(preference.getUser());
        userViewModel.setValue(preference.getUser());
    }

    public void logout(Activity activity) {
        // instance firebase auth
        FirebaseAuthHelper.getInstance();

        // sign out
        new Preference(activity).setUser(null);
        FirebaseAuthHelper.signOut();
    }
}
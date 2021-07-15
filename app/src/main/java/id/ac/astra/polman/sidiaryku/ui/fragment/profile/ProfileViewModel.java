package id.ac.astra.polman.sidiaryku.ui.fragment.profile;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import id.ac.astra.polman.sidiaryku.utils.FirebaseAuthHelper;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class ProfileViewModel extends ViewModel {
    public void logout(Activity activity) {
        // instance firebase auth
        FirebaseAuthHelper.getInstance();

        // sign out
        new Preference(activity).setUser(null);
        FirebaseAuthHelper.signOut();
    }
}
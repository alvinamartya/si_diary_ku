package id.ac.astra.polman.sidiaryku.utils;


import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsHelper {
    private final FirebaseAnalytics firebaseAnalytics;

    public FirebaseAnalyticsHelper(Activity activity) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    }

    public void logUser(String email) {
        firebaseAnalytics.setUserId(email);
    }

    public void logEventSearch(String key) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, key);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    public void logEventUserLogin(String email) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, email);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }

    public void logEventScreenView(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
}

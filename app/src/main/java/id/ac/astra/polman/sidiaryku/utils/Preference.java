package id.ac.astra.polman.sidiaryku.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import id.ac.astra.polman.sidiaryku.entity.UserEntity;

public class Preference {
    private final static String TAG = Preference.class.getSimpleName();
    private final static String key = "data_key";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    // attribute key
    private final static String user_key = "user_key";

    @SuppressLint("CommitPrefEdits")
    public Preference(Context context) {
        sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void setUser(UserEntity user) {
        String jsonUser = "";
        if (user != null) {
            jsonUser = gson.toJson(user);
        }

        editor.putString(user_key, jsonUser);
        editor.commit();
    }

    public UserEntity getUser() {
        String jsonUser = sharedPreferences.getString(user_key, "");

        Log.e(TAG, "getUser: " + jsonUser);

        if(!jsonUser.equals("")) {
            return gson.fromJson(jsonUser, UserEntity.class);
        }

        return null;
    }
}

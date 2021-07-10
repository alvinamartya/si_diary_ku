package id.ac.astra.polman.sidiaryku.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private final static String key = "key_data";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Preference(Context context) {
        sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}

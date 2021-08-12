package id.ac.astra.polman.sidiaryku.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.model.Place;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TagDao {
    private final static String TAG = TagDao.class.getSimpleName();
    private static TagDao INSTANCE;
    private static final MutableLiveData<List<String>> tagMutableLiveData = new MutableLiveData<>(new ArrayList<>());

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new TagDao();
        }
    }

    public static void clearTagLiveData() {
        tagMutableLiveData.postValue(new ArrayList<>());
    }

    public static void addTagLiveData(String tag) {
        List<String> tagList = tagMutableLiveData.getValue();
        if (tagList != null) {
            tagList.add(tag);
            tagMutableLiveData.postValue(tagList);
        }
    }

    public static void addTagLiveListData(List<String> tags) {
        Log.e(TAG, "addTagLiveListData: " + new Gson().toJson(tags));
        tagMutableLiveData.setValue(tags);
    }

    public static void removeTagLiveData(String tag) {
        List<String> tagList = tagMutableLiveData.getValue();
        if (tagList != null) {
            String removeTag = tag.substring(1);
            Log.e(TAG, "removeTagLiveData: " + removeTag);
            tagList.remove(removeTag);
            tagMutableLiveData.postValue(tagList);
        }
    }

    public static LiveData<List<String>> getTagLiveData() {
        return tagMutableLiveData;
    }
}

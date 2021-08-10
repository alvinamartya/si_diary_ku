package id.ac.astra.polman.sidiaryku.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

public class TagDao {
    private static TagDao INSTANCE;
    private static final MutableLiveData<List<String>> tagMutableLiveData = new MutableLiveData<>(new ArrayList<>());

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new TagDao();
        }
    }

    public static void clearTagLiveData() {
        List<String> tagList = tagMutableLiveData.getValue();
        if(tagList != null) {
            tagList.clear();
            tagMutableLiveData.postValue(tagList);
        }
    }

    public static void addTagLiveData(String tag) {
        List<String> tagList = tagMutableLiveData.getValue();
        if(tagList != null) {
            tagList.add(tag);
            tagMutableLiveData.postValue(tagList);
        }
    }

    public static void removeTagLiveData(String tag) {
        List<String> tagList = tagMutableLiveData.getValue();
        if(tagList != null) {
            tagList.remove(tag);
            tagMutableLiveData.postValue(tagList);
        }
    }

    public static LiveData<List<String>> getTagLiveData() {
        return tagMutableLiveData;
    }
}

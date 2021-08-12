package id.ac.astra.polman.sidiaryku.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;

public class DiaryDao {
    private final static String TAG = DiaryDao.class.getSimpleName();
    private static DiaryDao INSTANCE;
    private static final MutableLiveData<List<DiaryEntity>> diaryMutableLiveData = new MutableLiveData<>(new ArrayList<>());

    public static void refreshDiaryLiveData() {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            clearDiaryLiveData();
            diaryMutableLiveData.postValue(diaryList);
        }
    }

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new DiaryDao();
        }
    }

    public static void clearDiaryLiveData() {
        List<DiaryEntity> diaryEntityList = new ArrayList<>();
        diaryMutableLiveData.setValue(diaryEntityList);
    }

    public static void addDiaryLiveData(DiaryEntity diaryEntity) {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            diaryList.add(diaryEntity);
            diaryMutableLiveData.postValue(diaryList);
        }
    }

    public static void addAllDiaryLiveData(List<DiaryEntity> diaryEntityList) {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            diaryList.addAll(diaryEntityList);
            postDiaryLiveData(diaryList);
        }
    }

    public static void updateDiaryLiveData(DiaryEntity diaryEntity) {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            int indexOf = diaryEntity == null ? -1 : diaryList.indexOf(diaryEntity);
            if (indexOf > -1) {
                diaryList.set(indexOf, diaryEntity);
                postDiaryLiveData(diaryList);
            }
        }
    }

    public static DiaryEntity setProgress(String docId, int progress) {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        DiaryEntity result = null;
        if (diaryList != null) {
            result = diaryList
                    .stream()
                    .filter(x -> x.getId().equals(docId))
                    .findFirst()
                    .orElse(null);

            if(result != null) result.setProgress(progress);
        }
        return result;
    }

    public static DiaryEntity setImage(String docId, String imageUrl) {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        DiaryEntity result = null;
        if (diaryList != null) {
            result = diaryList
                    .stream()
                    .filter(x -> x.getId().equals(docId))
                    .findFirst()
                    .orElse(null);

            if(result != null) result.setImageUrl(imageUrl);
        }
        return result;
    }

    public static DiaryEntity getDiary(String docId) {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        DiaryEntity result = null;
        if (diaryList != null) {
            result = diaryList
                    .stream()
                    .filter(x -> x.getId().equals(docId))
                    .findFirst()
                    .orElse(null);
            assert result != null;
            Log.e(TAG, "getDiary: " + new Gson().toJson(result.getTagList()));
        }
        return result;
    }

    private static void postDiaryLiveData(List<DiaryEntity> diaryList) {
        diaryList.sort(Comparator
                .comparing(DiaryEntity::getDate).reversed());
        diaryMutableLiveData.postValue(diaryList);
    }

    public static void removeDiaryLiveData(String docId) {
        List<DiaryEntity> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            DiaryEntity diaryEntity = getDiary(docId);
            if(diaryEntity != null) diaryList.remove(diaryEntity);
            postDiaryLiveData(diaryList);
        }
    }

    public static LiveData<List<DiaryEntity>> getDiaryLiveData() {
        return diaryMutableLiveData;
    }
}

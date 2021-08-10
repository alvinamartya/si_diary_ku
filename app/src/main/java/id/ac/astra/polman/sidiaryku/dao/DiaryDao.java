package id.ac.astra.polman.sidiaryku.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import id.ac.astra.polman.sidiaryku.model.DiaryModel;

public class DiaryDao {
    private static DiaryDao INSTANCE;
    private static final MutableLiveData<List<DiaryModel>> diaryMutableLiveData = new MutableLiveData<>(new ArrayList<>());

    public static void initialize() {
        if (INSTANCE == null) {
            INSTANCE = new DiaryDao();
        }
    }

    public static void addDiaryLiveData(DiaryModel diaryModel) {
        List<DiaryModel> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            diaryList.add(diaryModel);
            diaryMutableLiveData.postValue(diaryList);
        }
    }

    public static void addAllDiaryLiveData(List<DiaryModel> diaryModelList) {
        List<DiaryModel> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            diaryList.addAll(diaryModelList);
            postDiaryLiveData(diaryList);
        }
    }

    public static void updateDiaryLiveData(DiaryModel diaryModel) {
        List<DiaryModel> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            int indexOf = diaryModel == null ? -1 : diaryList.indexOf(diaryModel);
            if (indexOf > -1) {
                diaryList.set(indexOf, diaryModel);
                postDiaryLiveData(diaryList);
            }
        }
    }

    public static DiaryModel setProgress(String docId, int progress) {
        List<DiaryModel> diaryList = diaryMutableLiveData.getValue();
        DiaryModel result = null;
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

    public static DiaryModel getDiary(String docId) {
        List<DiaryModel> diaryList = diaryMutableLiveData.getValue();
        DiaryModel result = null;
        if (diaryList != null) {
            result = diaryList
                    .stream()
                    .filter(x -> x.getId().equals(docId))
                    .findFirst()
                    .orElse(null);
        }
        return result;
    }

    private static void postDiaryLiveData(List<DiaryModel> diaryList) {
        diaryList.sort(Comparator
                .comparing(DiaryModel::getProgress)
                .thenComparing(DiaryModel::getDate).reversed());
        diaryMutableLiveData.postValue(diaryList);
    }

    public static void removeDiaryLiveData(String docId) {
        List<DiaryModel> diaryList = diaryMutableLiveData.getValue();
        if (diaryList != null) {
            DiaryModel diaryModel = getDiary(docId);
            if(diaryModel != null) diaryList.remove(diaryModel);
            postDiaryLiveData(diaryList);
        }
    }

    public static LiveData<List<DiaryModel>> getDiaryLiveData() {
        return diaryMutableLiveData;
    }
}

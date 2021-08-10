package id.ac.astra.polman.sidiaryku.ui.fragment.memory;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.DiaryModel;
import id.ac.astra.polman.sidiaryku.utils.DateHelper;
import id.ac.astra.polman.sidiaryku.utils.PopupMessageHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class MemoryViewModel extends ViewModel {
    private final static String TAG = MemoryViewModel.class.getSimpleName();
    private final MutableLiveData<List<DiaryModel>> diaryLiveData = new MutableLiveData<>(new ArrayList<>());
    private boolean isAllTimeMemory = true;

    public void viewAllTimeDiary() {
        DiaryDao.initialize();
        isAllTimeMemory = true;
        List<DiaryModel> diaryDaoList = DiaryDao.getDiaryLiveData().getValue();
        if (diaryDaoList != null) {
            diaryLiveData.postValue(diaryDaoList);
        }
    }

    public void viewOnThisDayDiary() {
        DiaryDao.initialize();
        isAllTimeMemory = false;
        List<DiaryModel> diaryDaoList = DiaryDao.getDiaryLiveData().getValue();
        if (diaryDaoList != null) {
            List<DiaryModel> onThisDiaryDaoList = diaryDaoList
                    .stream()
                    .filter(x -> x.getDate().contains(DateHelper.getCurrentDate("yyyy-MM-dd")))
                    .collect(Collectors.toList());
            diaryLiveData.postValue(onThisDiaryDaoList);
        }
    }

    public LiveData<List<DiaryModel>> getDiaryLiveData() {
        return diaryLiveData;
    }

    public boolean getIsAllTimeMemory() {
        return isAllTimeMemory;
    }

    public void deleteMemory(Context context, String docId) {
        String title = context.getString(R.string.delete_memory);
        String message = context.getString(R.string.are_you_sure);
        PopupMessageHelper.confirm(
                context,
                title,
                message,
                (dialogInterface, i) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    PreferenceHelper preference = new PreferenceHelper(context);
                    UserEntity userEntity = preference.getUser();

                    db
                            .collection("users")
                            .document(userEntity.getEmail())
                            .collection("diary")
                            .document(docId)
                            .delete()
                            .addOnSuccessListener(doc -> {
                                DiaryDao.initialize();
                                DiaryDao.removeDiaryLiveData(docId);
                                if (getIsAllTimeMemory()) viewAllTimeDiary();
                                else viewOnThisDayDiary();
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "deleteMemory: " + e.getLocalizedMessage());
                            });
                }, (dialogInterface, i) -> dialogInterface.dismiss()
        );
    }
}
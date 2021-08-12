package id.ac.astra.polman.sidiaryku.ui.fragment.edit_diary;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.model.NewDiaryModel;
import id.ac.astra.polman.sidiaryku.utils.DateHelper;
import id.ac.astra.polman.sidiaryku.utils.FirebaseStorageHelper;
import id.ac.astra.polman.sidiaryku.utils.PopupMessageHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class EditDiaryViewModel extends ViewModel {
    private final static String TAG = EditDiaryViewModel.class.getSimpleName();
    private final int maxLengthCharacter = 200;
    private final MutableLiveData<Integer> lengthCharacterLeftLiveData = new MutableLiveData<>(maxLengthCharacter);
    private final MutableLiveData<Bitmap> imageLiveData = new MutableLiveData<>();

    public LiveData<Integer> getLengthCharacterLeftLiveData() {
        return lengthCharacterLeftLiveData;
    }

    public void setLengthCharacterLeftLiveData(int lengthCharacter) {
        lengthCharacterLeftLiveData.postValue(maxLengthCharacter - lengthCharacter);
    }

    public LiveData<Bitmap> getImageLiveData() {
        return imageLiveData;
    }

    public void setImageLiveData(Bitmap bitmap) {
        imageLiveData.postValue(bitmap);
    }

    public LiveData<Boolean> saveDiary(Context context, boolean isHasImage, NewDiaryModel newDiaryModel, String docId) {
        MutableLiveData<Boolean> statuDiary = new MutableLiveData<>(false);
        String title = context.getString(R.string.new_diary);
        String diaryMustBeFilled = context.getString(R.string.diary_must_be_filled);

        if (newDiaryModel.getDiary().equals("")) {
            PopupMessageHelper
                    .show(context, title, diaryMustBeFilled);
        } else {
            DiaryEntity selectedDiaryEntity = DiaryDao.getDiary(docId);
            if (selectedDiaryEntity != null) {
                Map<String, Object> diary = new HashMap<>();
                diary.put("diary", newDiaryModel.getDiary());
                diary.put("address", newDiaryModel.getAddress());
                Log.e(TAG, "saveDiary: " + newDiaryModel.getTagList() );
                diary.put("tag_list", newDiaryModel.getTagList());
                diary.put("date", selectedDiaryEntity.getDate());
                if (selectedDiaryEntity.getImageUrl() != null && !selectedDiaryEntity.getImageUrl().equals(""))
                    diary.put("imageUrl", selectedDiaryEntity.getImageUrl());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                PreferenceHelper preferenceHelper = new PreferenceHelper(context);
                UserEntity userEntity = preferenceHelper.getUser();
                db
                        .collection("users")
                        .document(userEntity.getEmail())
                        .collection("diary")
                        .document(docId)
                        .set(diary)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DiaryDao.initialize();
                                int progress = isHasImage ? 0 : 100;
                                selectedDiaryEntity.setAddress(newDiaryModel.getAddress());
                                selectedDiaryEntity.setDiary(newDiaryModel.getDiary());
                                selectedDiaryEntity.setTagList(newDiaryModel.getTagList());
                                selectedDiaryEntity.setProgress(progress);
                                DiaryDao.updateDiaryLiveData(selectedDiaryEntity);

                                if (isHasImage)
                                    FirebaseStorageHelper.uploadStorage(context, docId, newDiaryModel.getImage());
                                statuDiary.postValue(true);
                            } else {
                                PopupMessageHelper.show(context, title, Objects.requireNonNull(task.getException()).getLocalizedMessage());
                            }
                        })
                        .addOnFailureListener(e -> PopupMessageHelper.show(context, title, e.getLocalizedMessage()));
            } else {
                statuDiary.postValue(false);
            }
        }

        return statuDiary;
    }
}
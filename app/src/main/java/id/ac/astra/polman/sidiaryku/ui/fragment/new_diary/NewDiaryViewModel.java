package id.ac.astra.polman.sidiaryku.ui.fragment.new_diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.DiaryModel;
import id.ac.astra.polman.sidiaryku.model.NewDiaryModel;
import id.ac.astra.polman.sidiaryku.utils.DateHelper;
import id.ac.astra.polman.sidiaryku.utils.FirebaseStorageHelper;
import id.ac.astra.polman.sidiaryku.utils.PopupMessageHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class NewDiaryViewModel extends ViewModel {
    private final static String TAG = NewDiaryViewModel.class.getSimpleName();
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

    public LiveData<Boolean> saveDiary(Context context, boolean isHasImage, NewDiaryModel newDiaryModel) {
        MutableLiveData<Boolean> statuDiary = new MutableLiveData<>(false);
        String title = context.getString(R.string.new_diary);
        String diaryMustBeFilled = context.getString(R.string.diary_must_be_filled);

        if (newDiaryModel.getDiary().equals("")) {
            PopupMessageHelper
                    .show(context, title, diaryMustBeFilled);
        } else {
            String date = DateHelper.getCurrentDate("yyyy-MM-dd hh:mm:ss");

            Map<String, Object> diary = new HashMap<>();
            diary.put("diary", newDiaryModel.getDiary());
            diary.put("address", newDiaryModel.getAddress());
            diary.put("tag_list", newDiaryModel.getTagList());
            diary.put("date", date);

            String id = UUID.randomUUID().toString();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            PreferenceHelper preferenceHelper = new PreferenceHelper(context);
            UserEntity userEntity = preferenceHelper.getUser();
            db
                    .collection("users")
                    .document(userEntity.getEmail())
                    .collection("diary")
                    .document(id)
                    .set(diary)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DiaryDao.initialize();
                            int progres = 0;
                            if (isHasImage) {
                                FirebaseStorageHelper.uploadStorage(context, id, newDiaryModel.getImage());
                            } else {
                                progres = 100;
                            }

                            DiaryModel diaryModel = new DiaryModel(id, newDiaryModel.getAddress(), date, newDiaryModel.getDiary(), "", newDiaryModel.getTagList(), progres);
                            DiaryDao.addDiaryLiveData(diaryModel);
                            statuDiary.postValue(true);
                        } else {
                            PopupMessageHelper.show(context, title, Objects.requireNonNull(task.getException()).getLocalizedMessage());
                        }
                    })
                    .addOnFailureListener(e -> PopupMessageHelper.show(context, title, e.getLocalizedMessage()));
        }

        return statuDiary;
    }

}
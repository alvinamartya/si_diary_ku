package id.ac.astra.polman.sidiaryku.ui.activity.main;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class MainActivityViewModel extends ViewModel {
    private final static String TAG = MainActivityViewModel.class.getSimpleName();

    public void loadDiary(Context context) {
        PreferenceHelper preferenceHelper = new PreferenceHelper(context);
        UserEntity userEntity = preferenceHelper.getUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db
                .collection("users")
                .document(userEntity.getEmail())
                .collection("diary")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DiaryDao.initialize();
                        DiaryDao.clearDiaryLiveData();
                        List<DiaryEntity> diaryEntities = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                        documentSnapshotList.forEach(x -> {
                            String id = x.getId();
                            String address = x.getString("address");
                            String date = x.getString("date");
                            String diary = x.getString("diary");
                            String imageUrl = x.getString("imageUrl") == null ? "" : x.getString("imageUrl");
                            List<String> tagList = (List<String>)x.get("tag_list");

                            diaryEntities.add(new DiaryEntity(id, address, date, diary, imageUrl, tagList, 100));
                        });
                        DiaryDao.addAllDiaryLiveData(diaryEntities);
                    } else {
                        Log.e(TAG, "loadDiary: " + Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "loadDiary: " + e.getLocalizedMessage()));
    }
}

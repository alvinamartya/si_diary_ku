package id.ac.astra.polman.sidiaryku.ui.fragment.memory;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.utils.PopupMessageHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class MemoryViewModel extends ViewModel {
    private final static String TAG = MemoryViewModel.class.getSimpleName();
    public LiveData<List<DiaryEntity>> getDiaryLiveData() {
        DiaryDao.initialize();
        return DiaryDao.getDiaryLiveData();
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
                            })
                            .addOnFailureListener(e -> Log.e(TAG, "deleteMemory: " + e.getLocalizedMessage()));
                }, (dialogInterface, i) -> dialogInterface.dismiss()
        );
    }
}
package id.ac.astra.polman.sidiaryku.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;

public class FirebaseStorageHelper {
    private static final String TAG = FirebaseStorageHelper.class.getSimpleName();

    public static void uploadStorage(Context context, String docId, Bitmap bitmap) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference ref = firebaseStorage.getReference(docId + ".jpeg");
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask
                .addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    DiaryDao.initialize();
                    DiaryEntity selectedDiary = DiaryDao.setProgress(docId, (int) progress);
                    if (selectedDiary != null) DiaryDao.updateDiaryLiveData(selectedDiary);
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "uploadStorage: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                    return ref.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Uri downloadUri = task.getResult();

                        PreferenceHelper preferenceHelper = new PreferenceHelper(context);
                        UserEntity userEntity = preferenceHelper.getUser();

                        Map<String, Object> diary = new HashMap<>();
                        diary.put("imageUrl", downloadUri.toString());
                        db
                                .collection("users")
                                .document(userEntity.getEmail())
                                .collection("diary")
                                .document(docId)
                                .update(diary)
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DiaryDao.initialize();
                                        DiaryEntity selectedDiary = DiaryDao.setImage(docId, downloadUri.toString());
                                        if (selectedDiary != null) DiaryDao.updateDiaryLiveData(selectedDiary);
                                        Log.i(TAG, "uploadStorage: success");
                                    } else {
                                        Log.e(TAG, "uploadStorage: " + Objects.requireNonNull(task2.getException()).getLocalizedMessage());
                                    }
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "uploadStorage: " + e.getLocalizedMessage()));
                    } else {
                        Log.e(TAG, "uploadStorage: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "uploadStorage: " + e.getLocalizedMessage());
                });
    }
}

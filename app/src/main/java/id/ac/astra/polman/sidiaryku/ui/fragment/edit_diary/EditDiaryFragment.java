package id.ac.astra.polman.sidiaryku.ui.fragment.edit_diary;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.dao.PlaceDao;
import id.ac.astra.polman.sidiaryku.dao.TagDao;
import id.ac.astra.polman.sidiaryku.databinding.FragmentEditDiaryBinding;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.model.NewDiaryModel;
import id.ac.astra.polman.sidiaryku.ui.activity.maps.MapsActivity;
import id.ac.astra.polman.sidiaryku.ui.adapter.TagAdapter;
import id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.new_tag.NewTagBottomSheetDialog;
import id.ac.astra.polman.sidiaryku.utils.MoveViewHelper;

import static android.app.Activity.RESULT_OK;

public class EditDiaryFragment extends Fragment {
    private final static String TAG = EditDiaryFragment.class.getSimpleName();
    private FragmentEditDiaryBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncherCamera, activityResultLauncherGallery;
    private boolean isHasImage = false;
    private String address = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditDiaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(new ViewModelProvider(this).get(EditDiaryViewModel.class));
        binding.backEditDiaryImage.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        String docId = requireArguments().getString("docId");
        TagDao.initialize();
        if(docId != null) {
            DiaryDao.initialize();
            DiaryEntity diaryEntity = DiaryDao.getDiary(docId);

            binding.typeWriteEditDiaryText.setText(diaryEntity.getDiary());
            if(diaryEntity.getAddress() != null && !diaryEntity.getAddress().equals("")) {
                binding.addressEditDiaryText.setText(diaryEntity.getAddress());
                address = diaryEntity.getAddress();
                binding.addressEditDiaryText.setVisibility(View.VISIBLE);
            }
            TagDao.addTagLiveListData(diaryEntity.getTagList());
            binding.getViewModel().setLengthCharacterLeftLiveData(Objects.requireNonNull(binding.typeWriteEditDiaryText.getText()).toString().length());

            if(diaryEntity.getImageUrl() != null && !diaryEntity.getImageUrl().equals("")) {
                binding.pictureEditDiaryImage.setVisibility(View.VISIBLE);
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this.requireContext());
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();

                Glide
                        .with(this)
                        .load(diaryEntity.getImageUrl())
                        .placeholder(circularProgressDrawable)
                        .into(binding.pictureEditDiaryImage);
            }
        }

        // count length of character left diary
        binding.getViewModel().getLengthCharacterLeftLiveData().observe(this.getViewLifecycleOwner(), lengthCharacter -> {
            binding.lengthCharacterNewDiaryText.setText(String.valueOf(lengthCharacter));
        });

        binding.typeWriteEditDiaryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.getViewModel().setLengthCharacterLeftLiveData(Objects.requireNonNull(binding.typeWriteEditDiaryText.getText()).toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.addTagsEditDiaryButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = this.getChildFragmentManager();
            NewTagBottomSheetDialog newTagBottomSheetDialog = new NewTagBottomSheetDialog();
            newTagBottomSheetDialog.show(fragmentManager, TAG);
        });
        binding.uploadPictureEditDiaryButton.setOnClickListener(v -> {
            String titleDialog = "";
            if (isHasImage) {
                CharSequence[] items = {
                        getString(R.string.take_photo),
                        getString(R.string.choose_from_library),
                        getString(R.string.delete_photo),
                        getString(R.string.cancel)
                };

                new AlertDialog
                        .Builder(this.requireContext())
                        .setTitle(titleDialog)
                        .setItems(items, (dialog, i) -> {
                            if (items[i].toString().equals(getString(R.string.take_photo))) {
                                takePhotoFromCamera();
                            } else if (items[i].toString().equals(getString(R.string.choose_from_library))) {
                                takePhotoFromGallery();
                            } else if (items[i].toString().equals(getString(R.string.delete_photo))) {
                                binding.getViewModel().setImageLiveData(null);
                            } else {
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                CharSequence[] items = {
                        getString(R.string.take_photo),
                        getString(R.string.choose_from_library),
                        getString(R.string.cancel)
                };

                new AlertDialog
                        .Builder(this.requireContext())
                        .setTitle(titleDialog)
                        .setItems(items, (dialog, i) -> {
                            if (items[i].toString().equals(getString(R.string.take_photo))) {
                                takePhotoFromCamera();
                            } else if (items[i].toString().equals(getString(R.string.choose_from_library))) {
                                takePhotoFromGallery();
                            } else {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        binding.pickALocationEditDiaryButton.setOnClickListener(v -> {
            MoveViewHelper.withoutFinish(this.requireContext(), MapsActivity.class);
        });

        // camera launcher
        activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                binding.getViewModel().setImageLiveData(bitmap);
            }
        });

        // gallery launcher
        activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImage = result.getData().getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (selectedImage != null) {
                    Cursor cursor = requireContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

                        binding.getViewModel().setImageLiveData(bitmap);
                        cursor.close();
                    }
                }
            }
        });

        // set place
        PlaceDao.initialize();
        PlaceDao.getPlaceLiveData().observe(this.getViewLifecycleOwner(), place -> {
            if (place == null && address.equals("")) {
                binding.addressEditDiaryText.setVisibility(View.GONE);
            } else {
                if(place != null) binding.addressEditDiaryText.setText(place.getAddress());
                binding.addressEditDiaryText.setVisibility(View.VISIBLE);
            }
        });

        // set image
        binding.getViewModel().getImageLiveData().observe(this.getViewLifecycleOwner(), bitmap -> {
            if (bitmap == null) {
                isHasImage = false;
                binding.pictureEditDiaryImage.setVisibility(View.GONE);
            } else {
                isHasImage = true;
                binding.pictureEditDiaryImage.setImageBitmap(bitmap);
                binding.pictureEditDiaryImage.setVisibility(View.VISIBLE);
            }
        });

        // set horizontal layout manager
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.tagRecyclerView.setLayoutManager(horizontalLayoutManager);

        // set list tag to recyclerview
        TagDao.getTagLiveData().observe(this.getViewLifecycleOwner(), tagList -> {
            Log.e(TAG, "onViewCreated: " + tagList.size());
            if (tagList.size() <= 0) {
                binding.tagRecyclerView.setVisibility(View.GONE);
            } else {
                binding.tagRecyclerView.setAdapter(new TagAdapter(this.requireContext(), tagList));
                binding.tagRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // save diary
        binding.saveEditDiaryButton.setOnClickListener(v -> {
            String address = binding.addressEditDiaryText.getText().toString();
            String diary = Objects.requireNonNull(binding.typeWriteEditDiaryText.getText()).toString();
            List<String> tagList = TagDao.getTagLiveData().getValue();
            Log.e(TAG, "onViewCreated: " + new Gson().toJson(tagList));
            Bitmap bitmap = isHasImage ? binding.getViewModel().getImageLiveData().getValue() : null;
            NewDiaryModel newDiaryModel = new NewDiaryModel(
                    diary,
                    address,
                    tagList,
                    bitmap
            );

            binding
                    .getViewModel()
                    .saveDiary(this.requireContext(), isHasImage, newDiaryModel, docId)
                    .observe(this.getViewLifecycleOwner(), statusDiary -> {
                        if (statusDiary) {
                            Navigation
                                    .findNavController(v)
                                    .navigate(R.id.action_editDiaryFragment_to_navigation_memory);
                        }
                    });
        });
    }
    // get image using camera
    private void takePhotoFromCamera() {
        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncherCamera.launch(intentTakePicture);
    }

    /// get image from gallery
    private void takePhotoFromGallery() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncherGallery.launch(intentFromGallery);
    }
}
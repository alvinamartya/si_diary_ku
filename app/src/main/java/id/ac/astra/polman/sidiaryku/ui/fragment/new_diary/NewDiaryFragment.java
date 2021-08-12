package id.ac.astra.polman.sidiaryku.ui.fragment.new_diary;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.PlaceDao;
import id.ac.astra.polman.sidiaryku.dao.TagDao;
import id.ac.astra.polman.sidiaryku.databinding.FragmentNewDiaryBinding;
import id.ac.astra.polman.sidiaryku.model.NewDiaryModel;
import id.ac.astra.polman.sidiaryku.ui.activity.maps.MapsActivity;
import id.ac.astra.polman.sidiaryku.ui.adapter.TagAdapter;
import id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.new_tag.NewTagBottomSheetDialog;
import id.ac.astra.polman.sidiaryku.utils.MoveViewHelper;

import static android.app.Activity.RESULT_OK;

public class NewDiaryFragment extends Fragment {
    private final static String TAG = NewDiaryFragment.class.getSimpleName();
    private FragmentNewDiaryBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncherCamera, activityResultLauncherGallery;
    private boolean isHasImage = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewDiaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(new ViewModelProvider(this).get(NewDiaryViewModel.class));
        binding.backNewDiaryImage.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        // count length of character left diary
        binding.getViewModel().getLengthCharacterLeftLiveData().observe(this.getViewLifecycleOwner(), lengthCharacter -> {
            Log.e(TAG, "onViewCreated: " + lengthCharacter);
            binding.lengthCharacterNewDiaryText.setText(String.valueOf(lengthCharacter));
        });

        binding.typeWriteNewDiaryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.getViewModel().setLengthCharacterLeftLiveData(Objects.requireNonNull(binding.typeWriteNewDiaryText.getText()).toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.addTagsNewDiaryButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = this.getChildFragmentManager();
            NewTagBottomSheetDialog newTagBottomSheetDialog = new NewTagBottomSheetDialog();
            newTagBottomSheetDialog.show(fragmentManager, TAG);
        });
        binding.uploadPictureNewDiaryButton.setOnClickListener(v -> {
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
        binding.pickALocationNewDiaryButton.setOnClickListener(v -> {
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
            if (place == null) {
                binding.addressNewDiaryText.setVisibility(View.GONE);
            } else {
                binding.addressNewDiaryText.setText(place.getAddress());
                binding.addressNewDiaryText.setVisibility(View.VISIBLE);
            }
        });

        // set image
        binding.getViewModel().getImageLiveData().observe(this.getViewLifecycleOwner(), bitmap -> {
            if (bitmap == null) {
                isHasImage = false;
                binding.pictureNewDiaryImage.setVisibility(View.GONE);
            } else {
                isHasImage = true;
                binding.pictureNewDiaryImage.setImageBitmap(bitmap);
                binding.pictureNewDiaryImage.setVisibility(View.VISIBLE);
            }
        });

        // set horizontal layout manager
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.tagRecyclerView.setLayoutManager(horizontalLayoutManager);

        // set list tag to recyclerview
        TagDao.initialize();
        TagDao.getTagLiveData().observe(this.getViewLifecycleOwner(), tagList -> {
            Log.e(TAG, "onViewCreated: " + tagList.size());
            if (tagList.size() <= 0) {
                binding.tagRecyclerView.setVisibility(View.GONE);
            } else {
                binding.tagRecyclerView.setAdapter(new TagAdapter(this.requireContext(), tagList, true));
                binding.tagRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // save diary
        binding.saveNewDiaryButton.setOnClickListener(v -> {
            String address = binding.addressNewDiaryText.getText().toString();
            String diary = Objects.requireNonNull(binding.typeWriteNewDiaryText.getText()).toString();
            List<String> tagList = TagDao.getTagLiveData().getValue();
            Bitmap bitmap = isHasImage ? binding.getViewModel().getImageLiveData().getValue() : null;
            NewDiaryModel newDiaryModel = new NewDiaryModel(
                    diary,
                    address,
                    tagList,
                    bitmap
            );

            binding
                    .getViewModel()
                    .saveDiary(this.requireContext(), isHasImage, newDiaryModel)
                    .observe(this.getViewLifecycleOwner(), statusDiary -> {
                        if (statusDiary) {
                            Navigation
                                    .findNavController(v)
                                    .navigate(R.id.action_newDiaryFragment_to_navigation_memory);
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

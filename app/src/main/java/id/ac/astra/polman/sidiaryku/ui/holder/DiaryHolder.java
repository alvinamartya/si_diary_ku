package id.ac.astra.polman.sidiaryku.ui.holder;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.DiaryModel;
import id.ac.astra.polman.sidiaryku.ui.adapter.TagAdapter;
import id.ac.astra.polman.sidiaryku.ui.fragment.memory.MemoryViewModel;
import id.ac.astra.polman.sidiaryku.utils.PopupMessageHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class DiaryHolder extends RecyclerView.ViewHolder {
    private final static String TAG = DiaryHolder.class.getSimpleName();
    private final TextView dateDiaryText, addressDiaryText, diaryText;
    private final ImageView pictureDiaryImage;
    private final RecyclerView tagRecyclerView;
    private final ConstraintLayout memoryLayout;

    public DiaryHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.diary_layout, parent, false));
        dateDiaryText = itemView.findViewById(R.id.date_diary_text);
        addressDiaryText = itemView.findViewById(R.id.address_diary_text);
        diaryText = itemView.findViewById(R.id.diary_diary_text);
        pictureDiaryImage = itemView.findViewById(R.id.picture_diary_image);
        tagRecyclerView = itemView.findViewById(R.id.tag_recycler_view);
        memoryLayout = itemView.findViewById(R.id.memory_layout);
    }

    public void bind(Context context, DiaryModel diaryModel, MemoryViewModel viewModel) {
        dateDiaryText.setText(diaryModel.getDate());
        diaryText.setText(diaryModel.getDiary());

        // diary doesn't have address
        if (diaryModel.getAddress() == null || diaryModel.getAddress().equals(""))
            addressDiaryText.setVisibility(View.GONE);

        // diary doesn't have image
        if (diaryModel.getImageUrl() == null || diaryModel.getImageUrl().equals(""))
            pictureDiaryImage.setVisibility(View.GONE);
        else {
            Glide
                    .with(itemView)
                    .load(diaryModel.getImageUrl())
                    .into(pictureDiaryImage);
        }

        // diary doesn't have tags
        if (diaryModel.getTagList().size() <= 0)
            tagRecyclerView.setVisibility(View.GONE);
        else {
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            tagRecyclerView.setLayoutManager(horizontalLayoutManager);
            tagRecyclerView.setAdapter(new TagAdapter(context, diaryModel.getTagList()));
        }

        memoryLayout.setOnClickListener(v -> {
            CharSequence[] items = {
                    context.getString(R.string.update_memory),
                    context.getString(R.string.delete_memory),
                    context.getString(R.string.share_memory),
                    context.getString(R.string.cancel)
            };

            new AlertDialog
                    .Builder(context)
                    .setItems(items, (dialog, i) -> {
                        if (items[i].toString().equals(context.getString(R.string.update_memory))) {
                        } else if (items[i].toString().equals(context.getString(R.string.delete_memory))) {
                            viewModel.deleteMemory(context, diaryModel.getId());
                        } else if (items[i].toString().equals(context.getString(R.string.share_memory))) {
                        } else {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }


}

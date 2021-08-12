package id.ac.astra.polman.sidiaryku.ui.holder;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.TagDao;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.ui.adapter.TagAdapter;
import id.ac.astra.polman.sidiaryku.ui.fragment.memory.MemoryViewModel;

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

    public void bind(Context context, DiaryEntity diaryEntity, MemoryViewModel viewModel) {
        dateDiaryText.setText(diaryEntity.getDate());
        diaryText.setText(diaryEntity.getDiary());

        // diary doesn't have address
        Log.e(TAG, "bind: " + diaryEntity.getAddress());
        if (diaryEntity.getAddress() == null || diaryEntity.getAddress().equals(""))
            addressDiaryText.setVisibility(View.GONE);
        else
            addressDiaryText.setText(diaryEntity.getAddress());

        // diary doesn't have image
        if (diaryEntity.getImageUrl() == null || diaryEntity.getImageUrl().equals(""))
            pictureDiaryImage.setVisibility(View.GONE);
        else {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            Glide
                    .with(itemView)
                    .load(diaryEntity.getImageUrl())
                    .placeholder(circularProgressDrawable)
                    .into(pictureDiaryImage);
        }

        // diary doesn't have tags
        if (diaryEntity.getTagList().size() <= 0)
            tagRecyclerView.setVisibility(View.GONE);
        else {
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            tagRecyclerView.setLayoutManager(horizontalLayoutManager);
            tagRecyclerView.setAdapter(new TagAdapter(context, diaryEntity.getTagList()));
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
                            TagDao.initialize();
                            TagDao.clearTagLiveData();

                            Bundle bundle = new Bundle();
                            bundle.putString("docId", diaryEntity.getId());

                            Navigation
                                    .findNavController(v)
                                    .navigate(R.id.action_navigation_memory_to_editDiaryFragment, bundle);
                        } else if (items[i].toString().equals(context.getString(R.string.delete_memory))) {
                            viewModel.deleteMemory(context, diaryEntity.getId());
                        } else if (items[i].toString().equals(context.getString(R.string.share_memory))) {
                        } else {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }


}

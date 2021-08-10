package id.ac.astra.polman.sidiaryku.model;

import android.graphics.Bitmap;

import java.util.List;

public class NewDiaryModel {
    private String diary;
    private String address;
    private List<String> tagList;
    private Bitmap image;

    public NewDiaryModel(String diary, String address, List<String> tagList, Bitmap image) {
        this.diary = diary;
        this.address = address;
        this.tagList = tagList;
        this.image = image;
    }

    public NewDiaryModel() {
    }

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

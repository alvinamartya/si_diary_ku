package id.ac.astra.polman.sidiaryku.model;

import java.util.List;

public class DiaryModel {
    private String id;
    private String address;
    private String date;
    private String diary;
    private String imageUrl;
    private List<String> tagList;
    private int progress;

    public DiaryModel(String id, String address, String date, String diary, String imageUrl, List<String> tagList, int progress) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.diary = diary;
        this.imageUrl = imageUrl;
        this.tagList = tagList;
        this.progress = progress;
    }

    public DiaryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

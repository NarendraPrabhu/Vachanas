package com.vachanasaahitya.vachanas.ui.bind;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailsHolder implements Parcelable{

    private String info;
    private String title;
    private boolean isFavorite;
    private boolean isVachana;

    public DetailsHolder(String info, String title, boolean isFavorite, boolean isVachana){
        this.info = info;
        this.title = title;
        this.isFavorite = isFavorite;
        this.isVachana = isVachana;
    }

    private DetailsHolder(Parcel parcel){
        this.info = parcel.readString();
        this.title = parcel.readString();
        this.isFavorite = parcel.readInt() == 1;
        this.isVachana = parcel.readInt() == 1;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean val){
        this.isFavorite = val;
    }

    public boolean isVachana() {
        return isVachana;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeString(title);
        dest.writeInt(isFavorite? 1 : 0);
        dest.writeInt(isVachana? 1 : 0);
    }

    public static final Parcelable.Creator<DetailsHolder> CREATOR = new Parcelable.Creator<DetailsHolder>() {
        @Override
        public DetailsHolder createFromParcel(Parcel source) {
            return new DetailsHolder(source);
        }

        @Override
        public DetailsHolder[] newArray(int size) {
            return new DetailsHolder[size];
        }
    };
}

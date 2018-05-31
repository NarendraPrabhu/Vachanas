package com.vachanasaahitya.vachanas.ui.bind;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailsHolder implements Parcelable{

    private String info;
    private String title;

    public DetailsHolder(String info, String title){
        this.info = info;
        this.title = title;
    }

    private DetailsHolder(Parcel parcel){
        this.info = parcel.readString();
        this.title = parcel.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeString(title);
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

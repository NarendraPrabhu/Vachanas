package com.vachanasaahitya.vachanas.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by narensmac on 01/03/18.
 */

public class Vachana implements Parcelable{

    private final String name;
    private final String vachana;
    private boolean isFavorite = false;

    public Vachana(String name, String vachana, boolean isFavorite){
        this.name = name;
        this.vachana =vachana;
        this.isFavorite = isFavorite;
    }

    protected Vachana(Parcel in) {
        name = in.readString();
        vachana = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(vachana);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vachana> CREATOR = new Creator<Vachana>() {
        @Override
        public Vachana createFromParcel(Parcel in) {
            return new Vachana(in);
        }

        @Override
        public Vachana[] newArray(int size) {
            return new Vachana[size];
        }
    };

    public String getVachana() {
        return vachana;
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Vachana){
            Vachana v = (Vachana)obj;
            return  this.name.equals(v.name) && this.vachana.equals(v.vachana);
        }
        return super.equals(obj);
    }
}

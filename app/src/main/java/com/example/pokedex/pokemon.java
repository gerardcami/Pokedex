package com.example.pokedex;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class pokemon implements Parcelable {
    public String id;
    public String name;
    public String[] types;
    public String imgUrl;

    protected pokemon(Parcel in) {
        id = in.readString();
        name = in.readString();
        types = in.createStringArray();
        imgUrl = in.readString();
    }

    public static final Creator<pokemon> CREATOR = new Creator<pokemon>() {
        @Override
        public pokemon createFromParcel(Parcel in) {
            return new pokemon(in);
        }

        @Override
        public pokemon[] newArray(int size) {
            return new pokemon[size];
        }
    };

    public String getName(){return name;}
    public String getId(){return id;}
    public String[] getTypes(){return types;}
    public String getImgUrl(){return imgUrl;}

    public pokemon(String id, String name, String[] types, String imgUrl){
        this.name = name;
        this.id = id;
        this.types = types;
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeStringArray(types);
        dest.writeString(imgUrl);
    }
}

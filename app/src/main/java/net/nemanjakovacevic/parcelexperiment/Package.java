package net.nemanjakovacevic.parcelexperiment;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nemanja on 3/21/15.
 */
public class Package implements Serializable, Parcelable {

    ArrayList<EmptyObject> mList;

    public Package(){

    }

    public Package(ArrayList<EmptyObject> list){
        this.mList = list;
    }

    public ArrayList<EmptyObject> getList() {
        return mList;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeSerializable(this.mList);
    }

    private Package(android.os.Parcel in) {
        this.mList = (ArrayList<EmptyObject>) in.readSerializable();
    }

    public static final Parcelable.Creator<Package> CREATOR = new Parcelable.Creator<Package>() {
        public Package createFromParcel(android.os.Parcel source) {
            return new Package(source);
        }

        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
}

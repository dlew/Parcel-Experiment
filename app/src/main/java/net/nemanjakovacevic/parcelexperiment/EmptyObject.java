package net.nemanjakovacevic.parcelexperiment;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nemanja on 3/19/15.
 */
public class EmptyObject implements Serializable, Parcelable {

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(android.os.Parcel dest, int flags) {
    }

    public EmptyObject() {
    }

    private EmptyObject(android.os.Parcel in) {
    }

    public static final Parcelable.Creator<EmptyObject> CREATOR = new Parcelable.Creator<EmptyObject>() {
        public EmptyObject createFromParcel(android.os.Parcel source) {
            return new EmptyObject(source);
        }

        public EmptyObject[] newArray(int size) {
            return new EmptyObject[size];
        }
    };
}

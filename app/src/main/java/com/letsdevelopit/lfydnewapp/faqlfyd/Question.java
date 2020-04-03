package com.letsdevelopit.lfydnewapp.faqlfyd;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    final String name;

    public Question(String name) {
        this.name = name;
    }

    protected Question(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
}

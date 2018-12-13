package com.imagesw.whatsstatus;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable;
public class my_pic_class implements Parcelable{
    public String image, thumbImage, id, title;


    public my_pic_class() {
    }

    public my_pic_class(String image, String thumbImage, String id) {
        this.image = image;
        this.thumbImage = thumbImage;
        this.id = id;
    }

    public my_pic_class(String title) {
        this.title = title;
    }


    protected my_pic_class(Parcel in) {
        image = in.readString();
        thumbImage = in.readString();
        id = in.readString();
        title = in.readString();
    }

    public static final Creator<my_pic_class> CREATOR = new Creator<my_pic_class>() {
        @Override
        public my_pic_class createFromParcel(Parcel in) {
            return new my_pic_class(in);
        }

        @Override
        public my_pic_class[] newArray(int size) {
            return new my_pic_class[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(thumbImage);
        dest.writeString(id);
        dest.writeString(title);
    }
}
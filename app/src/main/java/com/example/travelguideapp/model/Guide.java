package com.example.travelguideapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Guide implements Parcelable {
    String guideId;
    String name;
    String mobile;
    String email;
    String password;
    String identityProof;
    String imageUrl;
    String registrationDate;

    protected Guide(Parcel in) {
        guideId = in.readString();
        name = in.readString();
        mobile = in.readString();
        email = in.readString();
        password = in.readString();
        identityProof = in.readString();
        imageUrl = in.readString();
        registrationDate = in.readString();
    }

    public static final Creator<Guide> CREATOR = new Creator<Guide>() {
        @Override
        public Guide createFromParcel(Parcel in) {
            return new Guide(in);
        }

        @Override
        public Guide[] newArray(int size) {
            return new Guide[size];
        }
    };

    public String getGuideId() {
        return guideId;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    //alt+insert
    public Guide(String guideId, String name, String email, String mobile, String password, String identityProof, String imageUrl, String registrationDate) {
        this.guideId = guideId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.identityProof = identityProof;
        this.imageUrl = imageUrl;
        this.registrationDate = registrationDate;
    }

    public Guide() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getIdentityProof() {
        return identityProof;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Guide{" +
                "guideId='" + guideId + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", identityProof='" + identityProof + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(guideId);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(identityProof);
        dest.writeString(imageUrl);
        dest.writeString(registrationDate);
    }
}

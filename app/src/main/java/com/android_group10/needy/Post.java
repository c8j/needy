package com.android_group10.needy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post implements Serializable, Parcelable {
    private int postStatus;  // 1=Active, 2=In progress, 3=Finished (ready for rating), 4=rated by author, 5= rated by volunteer
    private String authorUID;
    private String description;
    private int serviceType;
    private String city;
    private String zipCode;
    private String incentive;
    private String volunteerUID;
    private String postUID;

    public Post() {
    }

    public Post(String authorUID, int postStatus, String description, int serviceType, String city, String zipCode, String incentive) {
        this.authorUID = authorUID;
        this.postStatus = postStatus;
        this.zipCode = zipCode;
        this.serviceType = serviceType;
        this.description = description;
        this.city = city;
        this.incentive = incentive;
        this.volunteerUID = "";
    }

    protected Post(Parcel in) {
        postStatus = in.readInt();
        authorUID = in.readString();
        description = in.readString();
        serviceType = in.readInt();
        city = in.readString();
        zipCode = in.readString();
        incentive = in.readString();
        volunteerUID = in.readString();
        postUID = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public void setVolunteer(String volunteerUID) {
        this.volunteerUID = volunteerUID;
    }

    public String getVolunteer() {
        return volunteerUID;
    }

    public int getPostStatus() {
        return postStatus;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
    }

    public String getIncentive() {
        return incentive;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostStatus(int postStatus) {
        this.postStatus = postStatus;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public int getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public String getPostUID() {
        return postUID;
    }

    public void setPostUID(String postUID) {
        this.postUID = postUID;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postStatus=" + getPostStatus() +
                ", authorUID='" + getAuthorUID() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", serviceType=" + getServiceType() +
                ", city='" + getCity() + '\'' +
                ", zipCode='" + getZipCode() + '\'' +
                ", incentive='" + getIncentive() + '\'' +
                ", volunteerUID='" + getVolunteer() + '\'' +
                ", postUID='" + getPostUID() + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postStatus", postStatus);
        result.put("author", authorUID);
        result.put("description", description);
        result.put("serviceType", serviceType);
        result.put("city", city);
        result.put("zipCode", zipCode);
        result.put("incentive", incentive);
        result.put("volunteer", volunteerUID);
        result.put("postUID", postUID);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postStatus);
        dest.writeString(authorUID);
        dest.writeString(description);
        dest.writeInt(serviceType);
        dest.writeString(city);
        dest.writeString(zipCode);
        dest.writeString(incentive);
        dest.writeString(volunteerUID);
        dest.writeString(postUID);
    }
}

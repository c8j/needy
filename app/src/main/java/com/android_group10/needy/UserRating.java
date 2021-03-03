package com.android_group10.needy;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserRating {
    private String userUID;
    private int ratingType;
    private int ratingValue;

    public UserRating(String userUID, int ratingType, int ratingValue){
        this.userUID = userUID;
        this.ratingType = ratingType;  // 1= author rated, 2 = volunteer rated
        this.ratingValue = ratingValue; // values 1-5
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public int getRatingType() {
        return ratingType;
    }

    public void setRatingType(int ratingType) {
        this.ratingType = ratingType;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("value", ratingValue);
        return result;
    }
}

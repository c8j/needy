package com.android_group10.needy;

public class UserRating {
    private User user;
    private int ratingType;
    private int ratingValue;

    public UserRating(User user, int ratingType, int ratingValue){
        this.user = user;
        this.ratingType = ratingType;
        this.ratingValue = ratingValue;
    }
}

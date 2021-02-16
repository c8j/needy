package com.android_group10.needy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {
    private int postStatus;  // 1=Active, 2=In progress, 3=Finished
    private final String authorEmail;
    private String description;
    private int serviceType;
    private String city;
    private String zipCode;
    private String incentive;
    private String volunteerEmail;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public Post(String authorEmail, String description, int serviceType, String city, String zipCode, String incentive){
        this.authorEmail = authorEmail;
        this.postStatus = 1;  //1 = Active
        this.zipCode = zipCode;
        this.serviceType = serviceType;
        this.description = description;
        this.city = city;
        this.incentive = incentive;
    }

    public void setVolunteer(String volunteerEmail) {
        this.volunteerEmail = volunteerEmail;
    }

    public String getVolunteer() {
        return volunteerEmail;
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

    public String getUserEmail() {
        return authorEmail;
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

    @Override
    public String toString() {
        return description;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", authorEmail);
        result.put("description", description);
        result.put("postStatus", postStatus);
        result.put("serviceType", serviceType);
        result.put("city", city);
        result.put("zipCode", zipCode);
        result.put("incentive", incentive);

        return result;
    }

}

package com.android_group10.needy;

public class Post {
    private int postId;
    private int postStatus;
    private User user;
    private String description;
    private ServiceType serviceType;
    private String city;
    private String zipCode;

    public Post(int postId, User user, String description, ServiceType serviceType, String city, String zipCode){
        this.postId = postId;
        this.user = user;
        this.postStatus = 1;  //1 = Active
        this.zipCode = zipCode;
        this.serviceType = serviceType;
        this.description = description;
        this.city = city;
        //   new City(city, zipCode);   //this might be changed if data is put into DB
    }

    public Post(){}

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPostStatus() {
        return postStatus;
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

    public User getUser() {
        return user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPostId() {
        return postId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postStatus=" + postStatus +
                ", description='" + description + '\'' +
                ", serviceType=" + serviceType +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}

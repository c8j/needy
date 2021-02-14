package com.android_group10.needy;

public class User {
    private String firstName;
    private String lastName;
    private int image;
    private String email;
    private String phone;
    private double authorRating;
    private double volunteerRating;

    public User(){}

    public User(String firstName, String phone, String email){
        this.email = email;
        this.firstName = firstName;
        this.phone = phone;
        this.authorRating = 5.0;
        this.volunteerRating = 5.0;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getImage() {
        return image;
    }

    public double getAuthorRating() {
       // authorRating = "request to database"
        return authorRating;
    }

    public double getVolunteerRating() {
        //volunteerRating = "request to database"
        return volunteerRating;
    }

    public void setAuthorRating(double authorRating) {
        ///send data to the DB and make a new calculation
        this.authorRating = authorRating;
    }

    public void setVolunteerRating(double volunteerRating) {
        ///send data to the DB and make a new calculation
        this.volunteerRating = volunteerRating;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

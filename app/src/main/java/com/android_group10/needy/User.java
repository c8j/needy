package com.android_group10.needy;

public class User {
    private int image;
    private String phone, city, email, lastName, firstName, password;
    private double authorRating;
    private double volunteerRating;
    private int zipCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public User(String email, String password, String firstName, String lastName, String phone, String city, int zipCode) {
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.zipCode = zipCode;
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

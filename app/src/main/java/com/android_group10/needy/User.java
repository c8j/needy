package com.android_group10.needy;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Serializable {
    private String phone, city, email, lastName, firstName, password;
    private double authorRating;
    private double volunteerRating;
    private int zipCode;

    public User() {}

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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public double getAuthorRating() {
        return authorRating;
    }

    public double getVolunteerRating() {
        return volunteerRating;
    }

    public void setAuthorRating(double authorRating) {
        this.authorRating = authorRating;
    }

    public void setVolunteerRating(double volunteerRating) {
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("city", city);
        result.put("email", email);
        result.put("zipCode", zipCode);
        result.put("phone", phone);
        result.put("authorRating", authorRating);
        result.put("volunteerRating", volunteerRating);
        return result;
    }
}

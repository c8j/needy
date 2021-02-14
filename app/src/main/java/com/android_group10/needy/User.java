package com.android_group10.needy;

public class User {

    private int image;
    //    private City city;
    private String phone, city, email, lastName, firstName, password;

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

    private int zipCode;

    public User() {

    }

    public User(String email, String password, String firstName, String lastName, String phone, String city, int zipCode) {
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.zipCode = zipCode;
    }

//   public void setCity(City city) {
    //       this.city = city;
    //   }

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

//    public City getCity() {
//        return city;
//    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                // ", city=" + city +
                ", phone='" + phone + '\'' +
                '}';
    }
}

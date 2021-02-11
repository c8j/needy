package com.android_group10.needy;

public class User {
    private String firstName;
    private String lastName;
    private int image;
    private String email;
//    private City city;
    private String phone;

    public User(){}

    public User(String firstName, String phone, String email){
        this.email = email;
        this.firstName = firstName;
        this.phone = phone;
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

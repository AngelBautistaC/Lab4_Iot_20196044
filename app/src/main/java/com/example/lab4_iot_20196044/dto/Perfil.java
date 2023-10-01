package com.example.lab4_iot_20196044.dto;

import java.io.Serializable;

public class Perfil implements Serializable {

    private String gender;
    private Name name;
    private Location location;
    private String email;
    private String phone;
    private Picture picture;

    public static class Name {
        public String title;
        public String first;
        public String last;
    }

    public static class Location {
        public String city;
        public String country;
    }

    public static class Picture {
        public String large;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}

package com.ahmadinfotech.moneytracker.model;

/**
 * Created by Zaheer Khorajiya on 15/1/18.
 */

public class User {

    private String uId;
    private String email;
    private String password;
    private String name;
    private String country;
    private String phone;
    private String birthday;
    private String hobby;

    public User(){

    }

    public User(String uId, String email, String password, String name, String country, String phone, String birthday, String hobby){
        this.uId = uId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.country = country;
        this.phone = phone;
        this.birthday = birthday;
        this.hobby = hobby;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

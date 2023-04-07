package com.example.javame4;

public class UserInfo {
    private int id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String address;
    private byte [] photo;

    public UserInfo(int id, String name, String surname,
                    String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.photo = new byte[0];
    }

    public UserInfo(int id, String name, String surname,
                    String phone, String email, String address, byte [] photo) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public byte[] getPhoto() {
        return photo;
    }
}
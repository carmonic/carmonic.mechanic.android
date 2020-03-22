package com.camsys.carmonic.mechanic.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



@Entity(tableName = "user")
public   class Users {

//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "did")
//    public int id;


    @PrimaryKey //(autoGenerate = true)
    @NonNull
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("token")
    @Expose
    private String token;

   // @Ignore
    @SerializedName("password")
    @Expose
    private String password;

   // @Ignore
    @SerializedName("longitude")
    @Expose
    private double longitude;

    //@Ignore
    @SerializedName("latitude")
    @Expose
    private double latitude;

    @Ignore
    @SerializedName("phone")
    @Expose
    private String  phone;

//        private double starRating;
//
//        private int estimatedDistanceInMins;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public void setPhoneNumber(String phone) {
        this.phone =  phone ;
    }

    public String getPhoneNumber() {
        return phone;
    }

}


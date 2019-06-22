package com.camsys.carmonic.mechanic.Model;

public class UserData {

    String FullName;
    String EmailAddress;
    String PhoneNumber;
    String Password;


    public void setFullName(String fullName){
        this.FullName =  fullName;
    }

    public void setEmailAddress(String emailAddress){
        this.EmailAddress =  emailAddress;
    }


    public void setPhoneNumber(String phoneNumber){
        this.PhoneNumber =  phoneNumber;
    }

    public void setPassword(String password){
        this.Password =  password;
    }


    public String getFullName(){
       return this.FullName;
    }

    public String getEmailAddress(){
        return this.EmailAddress;
    }


    public String getPhoneNumber(){
      return this.PhoneNumber;
    }

    public String getPassword(){
        return this.Password;
    }
}

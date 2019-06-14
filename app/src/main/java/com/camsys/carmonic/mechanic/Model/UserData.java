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
        this.PhoneNumber =  PhoneNumber;
    }

    public void setPassword(String password){
        this.Password =  password;
    }


    public String getFullName(){
       return FullName;
    }

    public String getEmailAddress(){
        return EmailAddress;
    }


    public String getPhoneNumber(String phoneNumber){
      return PhoneNumber;
    }

    public String getPassword(){
        return this.Password;
    }
}

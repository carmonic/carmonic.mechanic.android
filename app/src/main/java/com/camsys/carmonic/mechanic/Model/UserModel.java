package com.camsys.carmonic.mechanic.Model;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public  class UserModel {

    @SerializedName("user")
    @Expose
    private Users user;
    @SerializedName("authInfo")
    @Expose
    private AuthInfo authInfo;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthInfo authInfo) {
        this.authInfo = authInfo;
    }




    public class AuthInfo {

        @SerializedName("message")
        @Expose
        private String message;
        private String responseCode;

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String message) {
            this.responseCode = responseCode;
        }

    }

}




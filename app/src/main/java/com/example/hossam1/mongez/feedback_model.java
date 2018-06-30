package com.example.hossam1.mongez;

/**
 * Created by hossam1 on 6/29/18.
 */

public class feedback_model {

    String email ;
    String feedback;
    String phone_info ;

    public feedback_model() {
    }

    public void setPhone_info(String phone_info) {
        this.phone_info = phone_info;
    }

    public String getPhone_info() {

        return phone_info;
    }

    public feedback_model(String email, String feedback, String phone_info) {
        this.email = email;
        this.feedback = feedback;
        this.phone_info = phone_info;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

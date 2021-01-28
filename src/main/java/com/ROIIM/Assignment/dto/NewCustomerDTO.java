package com.ROIIM.Assignment.dto;

public class NewCustomerDTO {

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private String contact_number;

    private String paysafeID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getPaysafeID() {
        return paysafeID;
    }

    public void setPaysafeID(String paysafeID) {
        this.paysafeID = paysafeID;
    }
}

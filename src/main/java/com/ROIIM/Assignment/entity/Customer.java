package com.ROIIM.Assignment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String email;

    private String paysafeId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaysafeId() {
        return paysafeId;
    }

    public void setPaysafeId(String paysafeId) {
        this.paysafeId = paysafeId;
    }
}

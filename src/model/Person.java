/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Me
 */
public class Person {
    String firstName;
    String lastName;
    String personalNumber;
    String phoneNo;
    
    protected Person(String firstName, String lastName, String personalNumber, String phoneNo){
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.phoneNo = phoneNo;
    }
    protected Person(){
        
    }
    protected String getFirstName(){
        return firstName;
    }
    protected String getLastName(){
        return lastName;
    }
    protected String getPersNo(){
        return personalNumber;
    }
    protected String getPhoneNo(){
        return phoneNo;
    }
    
    @Override
    public String toString() {
        return "First name : " + firstName + "\t" + "Last name : " + lastName 
                + "\n" + "Identification : " + personalNumber + "\t" +  "Phone no: " + phoneNo;
    }
}

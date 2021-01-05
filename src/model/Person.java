
package model;

/**
 * This class represents a person.
 * Person is the subclass of the superclass <code>Student</code>
 * @author Frida Johansson
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
    /**
     * 
     * @return the persons name
     */
    protected String getFirstName(){
        return firstName;
    }
    /**
     * 
     * @return the persons last name
     */
    protected String getLastName(){
        return lastName;
    }
    /**
     * 
     * @return the persons personalnumber
     */
    protected String getPersNo(){
        return personalNumber;
    }
    /**
     * 
     * @return the persons phonenumber
     */
    protected String getPhoneNo(){
        return phoneNo;
    }
    
    @Override
    public String toString() {
        return "First name : " + firstName + "\t" + "Last name : " + lastName 
                + "\n" + "Identification : " + personalNumber + "\t" +  "Phone no: " + phoneNo;
    }
}

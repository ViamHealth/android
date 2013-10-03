package com.viamhealth.android.model;

/**
 * Created by naren on 03/10/13.
 */
public class User extends BaseModel {
    String username;
    String email;
    String firstName;
    String lastName;

    String name;
    Boolean isLoggedInUser = false;

    Profile profile;

    public Boolean isLoggedInUser() {
        return isLoggedInUser;
    }

    public void setLoggedInUser(Boolean loggedInUser) {
        isLoggedInUser = loggedInUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        if(name==null || name.isEmpty())
            name = firstName;
        else if(this.lastName==null || this.lastName.isEmpty())
            name = firstName;
        else
            name = firstName + " " + name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        if(name==null || name.isEmpty())
            name = lastName;
        else if(this.firstName==null || this.firstName.isEmpty())
            name = lastName;
        else
            name = name + " " + lastName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profile=" + profile +
                "} " + super.toString();
    }
}

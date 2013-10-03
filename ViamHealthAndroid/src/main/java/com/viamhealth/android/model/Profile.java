package com.viamhealth.android.model;

import android.content.Intent;

import com.facebook.model.GraphLocation;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;

import java.util.Date;

/**
 * Created by naren on 02/10/13.
 */
public class Profile {

    private String organization;
    private String mobileNumber;
    private Location location;
    private BloodGroup bloodGroup;
    private Date dob;
    private Gender gender;
    private String profilePicURL;

    private String fbProfileId;
    private String fbUsername;

    public String getFbUsername() {
        return fbUsername;
    }

    public void setFbUsername(String fbUsername) {
        this.fbUsername = fbUsername;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getFbProfileId() {
        return fbProfileId;
    }

    public void setFbProfileId(String fbProfileId) {
        this.fbProfileId = fbProfileId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public void setLocation(GraphLocation location) {
        if(this.location == null)
            this.location = new Location(location);
        else {
            this.location.setStreet(location.getStreet());
            this.location.setCity(location.getCity());
            this.location.setState(location.getState());
            this.location.setCountry(location.getCountry());
            this.location.setZip(location.getZip());

            this.location.setLattitude(location.getLatitude());
            this.location.setLongitude(location.getLongitude());
        }
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Profile() {

    }

    public class Location {
        String street;
        String city;
        String state;
        String country;
        String zip;

        double lattitude;
        double longitude;

        //free text for now
        String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public double getLattitude() {
            return lattitude;
        }

        public void setLattitude(double lattitude) {
            this.lattitude = lattitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public Location() {

        }

        public Location(GraphLocation location) {
            this.street = location.getStreet();
            this.city = location.getCity();
            this.state = location.getState();
            this.country = location.getCountry();
            this.zip = location.getZip();

            this.lattitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }

        public String toShortString() {
            String ss = this.city;

            if(ss==null || ss.isEmpty()) ss = this.state;
            else ss += "," + this.state;

            if(ss==null || ss.isEmpty()) ss = this.country;
            else ss += "," + this.country;

            if(ss==null || ss.isEmpty())
                ss = this.address;

            return ss;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "street='" + street + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", country='" + country + '\'' +
                    ", zip='" + zip + '\'' +
                    ", lattitude=" + lattitude +
                    ", longitude=" + longitude +
                    ", address='" + address + '\'' +
                    "} " + super.toString();
        }
    }

    @Override
    public String toString() {
        return "Profile{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", organization='" + organization + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", location=" + location +
                ", bloodGroup=" + bloodGroup +
                ", dob=" + dob +
                ", gender=" + gender +
                ", heightInCms=" + heightInCms +
                ", weightInKgs=" + weightInKgs +
                ", isLoggedInUser=" + isLoggedInUser +
                ", fbProfileId='" + fbProfileId + '\'' +
                "} " + super.toString();
    }
}

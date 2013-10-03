package com.viamhealth.android.model;

import com.facebook.model.GraphLocation;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Sex;

import java.util.Date;

/**
 * Created by naren on 02/10/13.
 */
public class ProfileData {

    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String organization;
    private String mobileNumber;
    private Location location;
    private BloodGroup bloodGroup;
    private Date dob;
    private Sex sex;

    private int heightInCms;
    private int weightInKgs;

    private Boolean isLoggedInUser = false;

    private String fbProfileId;

    public String getFbProfileId() {
        return fbProfileId;
    }

    public void setFbProfileId(String fbProfileId) {
        this.fbProfileId = fbProfileId;
    }

    public Boolean isLoggedInUser() {
        return isLoggedInUser;
    }

    public void setLoggedInUser(Boolean loggedInUser) {
        isLoggedInUser = loggedInUser;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getHeightInCms() {
        return heightInCms;
    }

    public void setHeightInCms(int heightInCms) {
        this.heightInCms = heightInCms;
    }

    public int getWeightInKgs() {
        return weightInKgs;
    }

    public void setWeightInKgs(int weightInKgs) {
        this.weightInKgs = weightInKgs;
    }

    public ProfileData() {

    }

    public class Location {
        String street;
        String city;
        String state;
        String country;
        String zip;

        double lattitude;
        double longitude;

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
            return this.city + "," + this.state + "," + this.country;
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
                    '}';
        }
    }
}

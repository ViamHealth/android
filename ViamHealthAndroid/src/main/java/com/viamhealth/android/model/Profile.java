package com.viamhealth.android.model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.model.GraphLocation;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;

import java.util.Date;

/**
 * Created by naren on 02/10/13.
 */
public class Profile implements Parcelable{

    private String organization;
    private String mobileNumber;
    private Location location;
    private BloodGroup bloodGroup = BloodGroup.O_Positive;
    private Date dob;
    private Gender gender = Gender.Male;
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

        this.location.setStreet(location.getStreet());
        this.location.setCity(location.getCity());
        this.location.setState(location.getState());
        this.location.setCountry(location.getCountry());
        this.location.setZip(location.getZip());

        this.location.setLattitude(location.getLatitude());
        this.location.setLongitude(location.getLongitude());
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
        this.location = new Location();
    }

    public class Location implements Parcelable{
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
                    "} ";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(street);
            dest.writeString(city);
            dest.writeString(state);
            dest.writeString(country);
            dest.writeString(zip);
            dest.writeString(address);
            dest.writeDouble(lattitude);
            dest.writeDouble(longitude);
        }

        public Location(Parcel in) {
            this.street = in.readString();
            this.city = in.readString();
            this.state = in.readString();
            this.country = in.readString();
            this.zip = in.readString();
            this.address = in.readString();
            this.lattitude = in.readDouble();
            this.longitude = in.readDouble();
        }

    }

    @Override
    public String toString() {
        return "Profile{" +
                "organization='" + organization + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", location=" + location +
                ", bloodGroup=" + bloodGroup +
                ", dob=" + dob +
                ", gender=" + gender +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", fbProfileId='" + fbProfileId + '\'' +
                ", fbUsername='" + fbUsername + '\'' +
                "} ";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        location.writeToParcel(dest, flags);

        dest.writeString(organization);
        dest.writeString(mobileNumber);
        Integer bg = bloodGroup==null?null:bloodGroup.value();
        dest.writeInt(bg);
        Integer g = gender==null?Gender.Male.value():gender.value();
        dest.writeInt(g);
        dest.writeValue(dob);
        dest.writeString(profilePicURL);
        dest.writeString(fbProfileId);
        dest.writeString(fbUsername);
    }

    public static final Parcelable.Creator<Profile> CREATOR
            = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public Profile(Parcel in) {
        this.location = new Location(in);
        this.organization = in.readString();
        this.mobileNumber = in.readString();
        this.bloodGroup = BloodGroup.get(in.readInt());
        this.gender = Gender.get(in.readInt());
        this.dob = (Date) in.readValue(null);
        this.profilePicURL = in.readString();
        this.fbProfileId = in.readString();
        this.fbUsername = in.readString();
    }
}

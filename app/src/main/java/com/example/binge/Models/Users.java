package com.example.binge.Models;

public class Users {
    String username,email, password,profilepic,userid, coverPhoto;
    int followerCount;
    Boolean typeFollowing=false,typeFollowers=false;

    public Users(String username, String email, String password, String profilepic, String userid) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilepic = profilepic;
        this.userid = userid;
    }
    public Users(){}

    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Boolean getTypeFollowers() {
        return typeFollowers;
    }

    public void setTypeFollowers(Boolean typeFollowers) {
        this.typeFollowers = typeFollowers;
    }

    public Boolean getTypeFollowing() {
        return typeFollowing;
    }

    public void setTypeFollowing(Boolean typeFollowing) {
        this.typeFollowing = typeFollowing;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPic(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}

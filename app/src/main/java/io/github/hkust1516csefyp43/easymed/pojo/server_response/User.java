package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Louis on 20/4/16.
 */
public class User {

//  @SeralizedName("birth_day")
//  @SeralizedName("birth_month")
//  @SeralizedName("birth_year")
//  @SeralizedName("phone_country_code")

  @SerializedName("user_id")            private String id;

  @SerializedName("username")           private String username;
  @SerializedName("email")              private String email;

  @SerializedName("honorific")          private String honorific;
  @SerializedName("first_name")         private String firstName;
  @SerializedName("middle_name")        private String middleName;
  @SerializedName("last_name")          private String lastName;
  @SerializedName("nickname")           private String nickname;

  @SerializedName("create_timestamp")   private Date createTimestamp;
  @SerializedName("gender_id")          private String genderId;
                                        private Gender gender;

  @SerializedName("image_id")           private String imageId;
  @SerializedName("role_id")            private String roleId;
                                        private Role role;

  @SerializedName("phone_number")       private String phoneNumber;

  @SerializedName("password")           private String password;  //for creating new patient

  public User() {
  }

  public User(String username, String firstName, String roleId, String password) {
    this.username = username;
    this.firstName = firstName;
    this.roleId = roleId;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getHonorific() {
    return honorific;
  }

  public void setHonorific(String honorific) {
    this.honorific = honorific;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Date getCreateTimestamp() {
    return createTimestamp;
  }

  public void setCreateTimestamp(Date createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  public String getGenderId() {
    return genderId;
  }

  public void setGenderId(String genderId) {
    this.genderId = genderId;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", honorific='" + honorific + '\'' +
        ", firstName='" + firstName + '\'' +
        ", middleName='" + middleName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", nickname='" + nickname + '\'' +
        ", createTimestamp=" + createTimestamp +
        ", genderId='" + genderId + '\'' +
        ", gender=" + gender +
        ", imageId='" + imageId + '\'' +
        ", roleId='" + roleId + '\'' +
        ", role=" + role +
        ", phoneNumber='" + phoneNumber + '\'' +
        '}';
  }
}

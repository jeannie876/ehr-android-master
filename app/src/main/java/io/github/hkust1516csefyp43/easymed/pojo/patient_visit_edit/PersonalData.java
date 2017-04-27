package io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit;


import java.io.Serializable;

/**
 * Created by Louis on 6/4/16.
 */
public class PersonalData implements Serializable {
  private static final long serialVersionUID = 2L;

  private String profilePicBase64;
  private String firstName;
  private String middleName;
  private String lastName;
  private String nativeName;
  private Integer tagNumber;
  private Integer birthYear;
  private Integer birthMonth;
  private Integer birthDate;
  private String genderId;
  private String address;
  private String phoneNumber;
  //TODO phone country code, status

  public PersonalData() {
    //empty constructor
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

  public String getNativeName() {
    return nativeName;
  }

  public void setNativeName(String nativeName) {
    this.nativeName = nativeName;
  }

  public Integer getTagNumber() {
    return tagNumber;
  }

  public void setTagNumber(Integer tagNumber) {
    this.tagNumber = tagNumber;
  }

  public Integer getBirthYear() {
    return birthYear;
  }

  public void setBirthYear(Integer birthYear) {
    this.birthYear = birthYear;
  }

  public Integer getBirthMonth() {
    return birthMonth;
  }

  public void setBirthMonth(Integer birthMonth) {
    this.birthMonth = birthMonth;
  }

  public Integer getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Integer birthDate) {
    this.birthDate = birthDate;
  }

  public String getGenderId() {
    return genderId;
  }

  public void setGenderId(String genderId) {
    this.genderId = genderId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getProfilePicBase64() {
    return profilePicBase64;
  }

  public void setProfilePicBase64(String profilePicBase64) {
    this.profilePicBase64 = profilePicBase64;
  }

  @Override
  public String toString() {
    return "PersonalData{" +
        "profilePicBase64='" + profilePicBase64 + '\'' +
        ", firstName='" + firstName + '\'' +
        ", middleName='" + middleName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", nativeName='" + nativeName + '\'' +
        ", tagNumber=" + tagNumber +
        ", birthYear=" + birthYear +
        ", birthMonth=" + birthMonth +
        ", birthDate=" + birthDate +
        ", genderId='" + genderId + '\'' +
        ", address='" + address + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        '}';
  }
}
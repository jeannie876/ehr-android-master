package io.github.hkust1516csefyp43.easymed.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 2/6/2016.
 */
public class LoginCredentials implements Serializable{
  @SerializedName("username") String username;
  @SerializedName("password") String password;
  @SerializedName("device_id") String deviceId;

  public LoginCredentials() {
  }

  public LoginCredentials(String username, String password, String deviceId) {
    this.username = username;
    this.password = password;
    this.deviceId = deviceId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  @Override
  public String toString() {
    return "LoginCredentials{" +
        "username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", deviceId='" + deviceId + '\'' +
        '}';
  }
}

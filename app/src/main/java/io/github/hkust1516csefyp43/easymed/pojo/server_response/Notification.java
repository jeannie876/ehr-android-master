package io.github.hkust1516csefyp43.easymed.pojo.server_response;

/**
 * Created by Louis on 20/4/16.
 */
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Louis on 22/3/16.
 */
public class Notification {
  @SerializedName("notification_id")  private String id;
  @SerializedName("message")          private String message;
  @SerializedName("user_id")          private String userId;
  @SerializedName("read")             private Boolean read;
  @SerializedName("remind_date")      private Date remindDate;

  public Notification(String id, String message, String userId, Boolean read, Date remindDate) {
    this.id = id;
    this.message = message;
    this.userId = userId;
    this.read = read;
    this.remindDate = remindDate;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Boolean getRead() {
    return read;
  }

  public void setRead(Boolean read) {
    this.read = read;
  }

  public Date getRemindDate() {
    return remindDate;
  }

  public void setRemindDate(Date remindDate) {
    this.remindDate = remindDate;
  }

  @Override
  public String toString() {
    return "Notification{" +
        "id='" + id + '\'' +
        ", message='" + message + '\'' +
        ", userId='" + userId + '\'' +
        ", read=" + read +
        ", remindDate=" + remindDate +
        '}';
  }
}
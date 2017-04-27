package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 3/6/2016.
 */
public class ServerRunningFor implements Serializable {
  //{"year":0,"week":0,"day":0,"hour":0,"minute":21,"second":51,"millisecond":760}
  @SerializedName("year") Integer year;
  @SerializedName("week") Integer week;
  @SerializedName("day") Integer day;
  @SerializedName("hour") Integer hour;
  @SerializedName("minute") Integer minute;
  @SerializedName("second") Integer second;
  @SerializedName("millisecond") Integer millisecond;

  public ServerRunningFor() {
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Integer getWeek() {
    return week;
  }

  public void setWeek(Integer week) {
    this.week = week;
  }

  public Integer getDay() {
    return day;
  }

  public void setDay(Integer day) {
    this.day = day;
  }

  public Integer getHour() {
    return hour;
  }

  public void setHour(Integer hour) {
    this.hour = hour;
  }

  public Integer getMinute() {
    return minute;
  }

  public void setMinute(Integer minute) {
    this.minute = minute;
  }

  public Integer getSecond() {
    return second;
  }

  public void setSecond(Integer second) {
    this.second = second;
  }

  public Integer getMillisecond() {
    return millisecond;
  }

  public void setMillisecond(Integer millisecond) {
    this.millisecond = millisecond;
  }

  @Override
  public String toString() {
    return "ServerRunningFor{" +
        "year=" + year +
        ", week=" + week +
        ", day=" + day +
        ", hour=" + hour +
        ", minute=" + minute +
        ", second=" + second +
        ", millisecond=" + millisecond +
        '}';
  }
}

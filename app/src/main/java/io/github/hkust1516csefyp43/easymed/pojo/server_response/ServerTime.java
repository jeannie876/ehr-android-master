package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Louis on 13/6/2016.
 */
public class ServerTime implements Serializable {
  @SerializedName("time") Date time;

  public ServerTime() {
  }

  public ServerTime(Date time) {
    this.time = time;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return "ServerTime{" +
        "time=" + time +
        '}';
  }
}

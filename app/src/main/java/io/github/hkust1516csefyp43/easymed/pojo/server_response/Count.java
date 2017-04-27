package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 24/5/2016.
 */
public class Count implements Serializable {
  @SerializedName("count")Integer count;

  public Count() {
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "Count{" +
        "count=" + count +
        '}';
  }
}

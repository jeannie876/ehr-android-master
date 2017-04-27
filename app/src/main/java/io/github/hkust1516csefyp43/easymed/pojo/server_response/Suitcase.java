package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 26/5/2016.
 */
public class Suitcase implements Serializable {
  @SerializedName("suitcase_id") String id;
  @SerializedName("suitcase_name")  String name;

  public Suitcase() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Suitcase{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}

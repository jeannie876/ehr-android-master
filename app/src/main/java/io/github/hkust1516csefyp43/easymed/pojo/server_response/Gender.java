package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Louis on 22/3/16.
 */
public class Gender {
  @SerializedName("gender_id")    private String id;
  @SerializedName("description")  private String gender;
  @SerializedName("biological_gender") private String biologicalGender;

  public Gender() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getBiologicalGender() {
    return biologicalGender;
  }

  public void setBiologicalGender(String biologicalGender) {
    this.biologicalGender = biologicalGender;
  }

  @Override
  public String toString() {
    return "Gender{" +
        "id='" + id + '\'' +
        ", gender='" + gender + '\'' +
        ", biologicalGender='" + biologicalGender + '\'' +
        '}';
  }


}

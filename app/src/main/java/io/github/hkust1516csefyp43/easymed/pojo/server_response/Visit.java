package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Louis on 2/4/16.
 */
public class Visit implements Serializable{
  private static final long serialVersionUID = 1L;

  @SerializedName("visit_id")
  String id;
  @SerializedName("tag")
  Integer tag;
  @SerializedName("patient_id")
  String patientId;
  @SerializedName("next_station")
  Integer nextStation;
  @SerializedName("create_timestamp")
  Date createTimestamp;

  public Visit() {
  }

  public Visit(String id, Integer tag, String patientId, Integer nextStation, Date createTimestamp) {
    this.id = id;
    this.tag = tag;
    this.patientId = patientId;
    this.nextStation = nextStation;
    this.createTimestamp = createTimestamp;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getTag() {
    return tag;
  }

  public void setTag(Integer tag) {
    this.tag = tag;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public Integer getNextStation() {
    return nextStation;
  }

  public void setNextStation(Integer nextStation) {
    this.nextStation = nextStation;
  }

  public Date getCreateTimestamp() {
    return createTimestamp;
  }

  public void setCreateTimestamp(Date createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  @Override
  public String toString() {

    //exception case for null
    return "Visit{" +
        "id='" + id + '\'' +
        ", tag=" + tag +
        ", patientId='" + patientId + '\'' +
        ", nextStation=" + nextStation +
        ", createTimestamp=" + createTimestamp +
        '}';
  }
}
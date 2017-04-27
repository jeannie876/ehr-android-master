package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Louis on 24/3/16.
 */
public class Triage implements Serializable{
  private static final long serialVersionUID = 1L;

  @SerializedName("triage_id")                String id;
  @SerializedName("user_id")                  String userId;
  @SerializedName("visit_id")                 String visitId;
  @SerializedName("chief_complains")          String chiefComplaints;
  @SerializedName("diastolic")                Integer diastolic;
  @SerializedName("systolic")                 Integer systolic;
  @SerializedName("start_timestamp")          Date startTime;
  @SerializedName("end_timestamp")            Date endTime;
  @SerializedName("edited_in_consultation")   Boolean editedInConsultation;
  @SerializedName("head_circumference")       Double headCircumference;
  @SerializedName("heart_rate")               Integer heartRate;
  @SerializedName("height")                   Double height;
  @SerializedName("weight")                   Double weight;
  @SerializedName("last_deworming_tablet")    Date lastDewormingTabletDate;
  @SerializedName("remark")                   String remark;
  @SerializedName("respiratory_rate")         Integer respiratoryRate;
  @SerializedName("spo2")                     Integer spo2;
  @SerializedName("temperature")              Double temperature;
  @SerializedName("blood_sugar")              Double bloodSugar;

  public Triage() {
    //empty constructor
  }

  public Triage(String id, String userId, String visitId, String chiefComplaints, Integer diastolic, Integer systolic, Date startTime, Date endTime, Boolean editedInConsultation, Double headCircumference, Integer heartRate, Double height, Double weight, Date lastDewormingTabletDate, String remark, Integer respiratoryRate, Integer spo2, Double temperature, Double bloodSugar) {
    this.id = id;
    this.userId = userId;
    this.visitId = visitId;
    this.chiefComplaints = chiefComplaints;
    this.diastolic = diastolic;
    this.systolic = systolic;
    this.startTime = startTime;
    this.endTime = endTime;
    this.editedInConsultation = editedInConsultation;
    this.headCircumference = headCircumference;
    this.heartRate = heartRate;
    this.height = height;
    this.weight = weight;
    this.lastDewormingTabletDate = lastDewormingTabletDate;
    this.remark = remark;
    this.respiratoryRate = respiratoryRate;
    this.spo2 = spo2;
    this.temperature = temperature;
    this.bloodSugar = bloodSugar;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getVisitId() {
    return visitId;
  }

  public void setVisitId(String visitId) {
    this.visitId = visitId;
  }

  public String getChiefComplaints() {
    return chiefComplaints;
  }

  public void setChiefComplaints(String chiefComplaints) {
    this.chiefComplaints = chiefComplaints;
  }

  public Integer getDiastolic() {
    return diastolic;
  }

  public void setDiastolic(Integer diastolic) {
    this.diastolic = diastolic;
  }

  public Integer getSystolic() {
    return systolic;
  }

  public void setSystolic(Integer systolic) {
    this.systolic = systolic;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Boolean getEditedInConsultation() {
    return editedInConsultation;
  }

  public void setEditedInConsultation(Boolean editedInConsultation) {
    this.editedInConsultation = editedInConsultation;
  }

  public Double getHeadCircumference() {
    return headCircumference;
  }

  public void setHeadCircumference(Double headCircumference) {
    this.headCircumference = headCircumference;
  }

  public Integer getHeartRate() {
    return heartRate;
  }

  public void setHeartRate(Integer heartRate) {
    this.heartRate = heartRate;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public Date getLastDewormingTabletDate() {
    return lastDewormingTabletDate;
  }

  public void setLastDewormingTabletDate(Date lastDewormingTabletDate) {
    this.lastDewormingTabletDate = lastDewormingTabletDate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getRespiratoryRate() {
    return respiratoryRate;
  }

  public void setRespiratoryRate(Integer respiratoryRate) {
    this.respiratoryRate = respiratoryRate;
  }

  public Integer getSpo2() {
    return spo2;
  }

  public void setSpo2(Integer spo2) {
    this.spo2 = spo2;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public Double getBloodSugar() {
    return bloodSugar;
  }

  public void setBloodSugar(Double bloodSugar) {
    this.bloodSugar = bloodSugar;
  }

  @Override
  public String toString() {
    return "Triage{" +
            "id='" + id + '\'' +
            ", userId='" + userId + '\'' +
            ", visitId='" + visitId + '\'' +
            ", chiefComplaints='" + chiefComplaints + '\'' +
            ", diastolic=" + diastolic +
            ", systolic=" + systolic +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", editedInConsultation=" + editedInConsultation +
            ", headCircumference=" + headCircumference +
            ", heartRate=" + heartRate +
            ", height=" + height +
            ", weight=" + weight +
            ", lastDewormingTabletDate=" + lastDewormingTabletDate +
            ", remark='" + remark + '\'' +
            ", respiratoryRate=" + respiratoryRate +
            ", spo2=" + spo2 +
            ", temperature=" + temperature +
            ", bloodSugar=" + bloodSugar +
            '}';
  }
}
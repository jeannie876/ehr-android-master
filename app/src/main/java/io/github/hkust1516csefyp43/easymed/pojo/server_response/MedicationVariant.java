package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 22/4/16.
 */
public class MedicationVariant implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("form")                     String form;
  @SerializedName("medication_id")            String medicationId;
  @SerializedName("medication_variant_id")    String id;
  @SerializedName("name")                     String name;
  @SerializedName("remark")                   String remark;
  @SerializedName("stock")                    Integer stock;
  @SerializedName("strength")                 String strength;
  @SerializedName("suitcase_id")              String suitcaseId;
  @SerializedName("user_id")                  String userId;
  String medicationName;

  public MedicationVariant() {
  }

  public String getForm() {
    return form;
  }

  public void setForm(String form) {
    this.form = form;
  }

  public String getMedicationId() {
    return medicationId;
  }

  public void setMedicationId(String medicationId) {
    this.medicationId = medicationId;
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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public String getStrength() {
    return strength;
  }

  public void setStrength(String strength) {
    this.strength = strength;
  }

  public String getSuitcaseId() {
    return suitcaseId;
  }

  public void setSuitcaseId(String suitcaseId) {
    this.suitcaseId = suitcaseId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getMedicationName() {
    return medicationName;
  }

  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  @Override
  public String toString() {
    return "MedicationVariant{" +
        "form='" + form + '\'' +
        ", medicationId='" + medicationId + '\'' +
        ", id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", remark='" + remark + '\'' +
        ", stock=" + stock +
        ", strength='" + strength + '\'' +
        ", suitcaseId='" + suitcaseId + '\'' +
        ", userId='" + userId + '\'' +
        ", medicationName='" + medicationName + '\'' +
        '}';
  }
}

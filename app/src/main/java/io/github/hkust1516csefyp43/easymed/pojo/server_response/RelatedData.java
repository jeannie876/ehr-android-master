package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 27/4/16.
 */
public class RelatedData implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("category")           Integer category;
  @SerializedName("consultation_id")    String consultationId;
  @SerializedName("data")               String data;
  @SerializedName("rd_id")              String id;
  @SerializedName("remark")             String remark;

  public RelatedData() {
  }

  public Integer getCategory() {
    return category;
  }

  public void setCategory(Integer category) {
    this.category = category;
  }

  public String getConsultationId() {
    return consultationId;
  }

  public void setConsultationId(String consultationId) {
    this.consultationId = consultationId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public String toString() {
    return "RelatedData{" +
        "category=" + category +
        ", consultationId='" + consultationId + '\'' +
        ", data='" + data + '\'' +
        ", id='" + id + '\'' +
        ", remark='" + remark + '\'' +
        '}';
  }
}

package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Louis on 25/3/16.
 */
public class BlockedDevice implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("blocked_device_id")
  private String id;
  @SerializedName("reporter_id")
  private String reporterId;
  @SerializedName("victim_id")
  private String victimId;
  @SerializedName("expiry_timestamp")
  private Date expiryTimestamp;
  @SerializedName("create_timestamp")
  private Date createTimestamp;
  @SerializedName("remark")
  private String remark;

  public BlockedDevice(String id, String reporterId, String victimId, Date expiryTimestamp, Date createTimestamp, String remark) {
    this.id = id;
    this.reporterId = reporterId;
    this.victimId = victimId;
    this.expiryTimestamp = expiryTimestamp;
    this.createTimestamp = createTimestamp;
    this.remark = remark;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getReporterId() {
    return reporterId;
  }

  public void setReporterId(String reporterId) {
    this.reporterId = reporterId;
  }

  public String getVictimId() {
    return victimId;
  }

  public void setVictimId(String victimId) {
    this.victimId = victimId;
  }

  public Date getExpiryTimestamp() {
    return expiryTimestamp;
  }

  public void setExpiryTimestamp(Date expiryTimestamp) {
    this.expiryTimestamp = expiryTimestamp;
  }

  public Date getCreateTimestamp() {
    return createTimestamp;
  }

  public void setCreateTimestamp(Date createTimestamp) {
    this.createTimestamp = createTimestamp;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public String toString() {
    return "BlockedDevice{" +
        "id='" + id + '\'' +
        ", reporterId='" + reporterId + '\'' +
        ", victimId='" + victimId + '\'' +
        ", expiryTimestamp=" + expiryTimestamp +
        ", createTimestamp=" + createTimestamp +
        ", remark='" + remark + '\'' +
        '}';
  }
}

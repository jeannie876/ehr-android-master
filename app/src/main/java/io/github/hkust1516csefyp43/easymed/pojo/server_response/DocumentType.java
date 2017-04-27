package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 25/3/16.
 */
public class DocumentType implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("document_type_id")
  private String id;
  @SerializedName("type")
  private String type;

  public DocumentType(String id, String type) {
    this.id = id;
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "DocumentType{" +
        "id='" + id + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
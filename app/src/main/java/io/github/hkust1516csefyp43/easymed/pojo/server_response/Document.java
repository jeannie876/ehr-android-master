package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Louis on 24/4/16.
 */
public class Document implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("document_id")      String id;
  @SerializedName("document")         String documentInHtml;
  @SerializedName("document_type")    String document_type;
  @SerializedName("patient_id")       String patientId;

  public Document() {
  }

  public Document(String documentInHtml) {
    this.documentInHtml = documentInHtml;
  }

  public Document(String documentInHtml, String document_type, String patientId) {
    this.documentInHtml = documentInHtml;
    this.document_type = document_type;
    this.patientId = patientId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDocumentInHtml() {
    return documentInHtml;
  }

  public void setDocumentInHtml(String documentInHtml) {
    this.documentInHtml = documentInHtml;
  }

  public String getDocument_type() {
    return document_type;
  }

  public void setDocument_type(String document_type) {
    this.document_type = document_type;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  @Override
  public String toString() {
    return "Document{" +
        "id='" + id + '\'' +
        ", documentInHtml='" + documentInHtml + '\'' +
        ", document_type='" + document_type + '\'' +
        ", patientId='" + patientId + '\'' +
        '}';
  }
}

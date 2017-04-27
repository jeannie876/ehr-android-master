package io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit;

import java.io.Serializable;

/**
 * Created by admin on 8/6/2016.
 */

public class Document implements Serializable{
    private String docID;
    private String docHTML;

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getDocHTML() {
        return docHTML;
    }

    public void setDocHTML(String docHTML) {
        this.docHTML = docHTML;
    }

    public Document(String docID, String docHTML) {

        this.docID = docID;
        this.docHTML = docHTML;
    }

    @Override
    public String toString() {
        return "Document{" +
                "docID='" + docID + '\'' +
                ", docHTML='" + docHTML + '\'' +
                '}';
    }
}

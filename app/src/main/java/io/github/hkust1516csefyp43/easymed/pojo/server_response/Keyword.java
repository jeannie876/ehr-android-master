package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Louis on 22/3/16.
 */
public class Keyword {
  @SerializedName("keyword_id")				      private String  id;
  @SerializedName("keyword")					      private String  keyword;
  @SerializedName("advice")					        private Boolean advice;
  @SerializedName("allergen")					      private Boolean allergen;
  @SerializedName("chief_complain")			    private Boolean chiefComplain;
  @SerializedName("diagnosis")				      private Boolean diagnosis;
  @SerializedName("education")				      private Boolean education;
  @SerializedName("follow_up")				      private Boolean followUp;
  @SerializedName("general")					      private Boolean general;
  @SerializedName("investigation")			    private Boolean investigation;
  @SerializedName("medication_form")			  private Boolean medicationForm;
  @SerializedName("medication_frequency")		private Boolean medicationFrequency;
  @SerializedName("medication_route")			  private Boolean medicationRoute;
  @SerializedName("relationship_type")		  private Boolean relationshipTypes;
  @SerializedName("screening")				      private Boolean screening;
  @SerializedName("unit")						        private Boolean unit;

  public Keyword(String id, String keyword, Boolean advice, Boolean allergen, Boolean chiefComplain, Boolean diagnosis, Boolean education, Boolean followUp, Boolean general, Boolean investigation, Boolean medicationForm, Boolean medicationFrequency, Boolean medicationRoute, Boolean relationshipTypes, Boolean screening, Boolean unit) {
    this.id = id;
    this.keyword = keyword;
    this.advice = advice;
    this.allergen = allergen;
    this.chiefComplain = chiefComplain;
    this.diagnosis = diagnosis;
    this.education = education;
    this.followUp = followUp;
    this.general = general;
    this.investigation = investigation;
    this.medicationForm = medicationForm;
    this.medicationFrequency = medicationFrequency;
    this.medicationRoute = medicationRoute;
    this.relationshipTypes = relationshipTypes;
    this.screening = screening;
    this.unit = unit;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Boolean getAdvice() {
    return advice;
  }

  public void setAdvice(Boolean advice) {
    this.advice = advice;
  }

  public Boolean getAllergen() {
    return allergen;
  }

  public void setAllergen(Boolean allergen) {
    this.allergen = allergen;
  }

  public Boolean getChiefComplain() {
    return chiefComplain;
  }

  public void setChiefComplain(Boolean chiefComplain) {
    this.chiefComplain = chiefComplain;
  }

  public Boolean getDiagnosis() {
    return diagnosis;
  }

  public void setDiagnosis(Boolean diagnosis) {
    this.diagnosis = diagnosis;
  }

  public Boolean getEducation() {
    return education;
  }

  public void setEducation(Boolean education) {
    this.education = education;
  }

  public Boolean getFollowUp() {
    return followUp;
  }

  public void setFollowUp(Boolean followUp) {
    this.followUp = followUp;
  }

  public Boolean getGeneral() {
    return general;
  }

  public void setGeneral(Boolean general) {
    this.general = general;
  }

  public Boolean getInvestigation() {
    return investigation;
  }

  public void setInvestigation(Boolean investigation) {
    this.investigation = investigation;
  }

  public Boolean getMedicationForm() {
    return medicationForm;
  }

  public void setMedicationForm(Boolean medicationForm) {
    this.medicationForm = medicationForm;
  }

  public Boolean getMedicationFrequency() {
    return medicationFrequency;
  }

  public void setMedicationFrequency(Boolean medicationFrequency) {
    this.medicationFrequency = medicationFrequency;
  }

  public Boolean getMedicationRoute() {
    return medicationRoute;
  }

  public void setMedicationRoute(Boolean medicationRoute) {
    this.medicationRoute = medicationRoute;
  }

  public Boolean getRelationshipTypes() {
    return relationshipTypes;
  }

  public void setRelationshipTypes(Boolean relationshipTypes) {
    this.relationshipTypes = relationshipTypes;
  }

  public Boolean getScreening() {
    return screening;
  }

  public void setScreening(Boolean screening) {
    this.screening = screening;
  }

  public Boolean getUnit() {
    return unit;
  }

  public void setUnit(Boolean unit) {
    this.unit = unit;
  }

  @Override
  public String toString() {
    return "Keyword{" +
        "id='" + id + '\'' + ", keyword='" + keyword + '\'' + ", advice=" + advice +
        ", allergen=" + allergen + ", chiefComplain=" + chiefComplain +
        ", diagnosis=" + diagnosis + ", education=" + education + ", followUp=" + followUp +
        ", general=" + general + ", investigation=" + investigation +
        ", medicationForm=" + medicationForm + ", medicationFrequency=" + medicationFrequency +
        ", medicationRoute=" + medicationRoute + ", relationshipTypes=" + relationshipTypes +
        ", screening=" + screening + ", unit=" + unit + '}';
  }
}
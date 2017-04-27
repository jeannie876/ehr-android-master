package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Louis on 20/4/16.
 */
public class Consultation implements Serializable{
  private static final long serialVersionUID = 2L;

  @SerializedName("consultation_id")          String id;
  @SerializedName("user_id")                  String userId;
  @SerializedName("visit_id")                 String visitId;
  @SerializedName("triage_id")                String triageId;
  @SerializedName("start_timestamp")          Date startTime;
  @SerializedName("end_timestamp")            Date endTime;
  @SerializedName("pe_cardio")                String peCardio;
  @SerializedName("pe_ent")                   String peEnt;
  @SerializedName("pe_gastro")                String peGastro;
  @SerializedName("pe_general")               String peGeneral;
  @SerializedName("pe_genital")               String peGenital;
  @SerializedName("pe_other")                 String peOther;
  @SerializedName("pe_respiratory")           String peRespiratory;
  @SerializedName("pe_skin")                  String peSkin;
  @SerializedName("preg_breast_feeding")      Boolean pregBreastFeeding;
  @SerializedName("preg_contraceptive")       Boolean pregContraceptive;
  @SerializedName("preg_curr_preg")           Boolean pregCurrPreg;
  @SerializedName("preg_gestation")           Integer pregGestation;
  @SerializedName("preg_lmp")                 Date pregLmp;
  @SerializedName("preg_num_abortion")        Integer pregNumAbortion;
  @SerializedName("preg_num_live_birth")      Integer pregNumLiveBirth;
  @SerializedName("preg_num_miscarriage")     Integer pregNumMiscarriage;
  @SerializedName("preg_num_preg")            Integer pregNumPreg;
  @SerializedName("preg_num_still_birth")     Integer pregNumStillBirth;
  @SerializedName("preg_remark")              String pregRemark;
  @SerializedName("remark")                   String remark;
  @SerializedName("rf_alertness")             String rfAlertness;
  @SerializedName("rf_breathing")             String rfBreathing;
  @SerializedName("rf_circulation")           String rfCirculation;
  @SerializedName("rf_defg")                  String rfDefg;
  @SerializedName("rf_dehydration")           String rfDehydration;
  @SerializedName("ros_general")              String rosGeneral;
  @SerializedName("ros_respi")                String rosRespi;
  @SerializedName("ros_cardio")               String rosCardio;
  @SerializedName("ros_gastro")               String rosGastro;
  @SerializedName("ros_genital")              String rosGenital;
  @SerializedName("ros_ent")                  String rosEnt;
  @SerializedName("ros_skin")                 String rosSkin;
  @SerializedName("ros_locomotor")            String rosLocomotor;
  @SerializedName("ros_neurology")            String rosNeruology;
  @SerializedName("ros_other")                String rosOther;

  public Consultation() {
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

  public String getTriageId() {
    return triageId;
  }

  public void setTriageId(String triageId) {
    this.triageId = triageId;
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

  public String getPeCardio() {
    return peCardio;
  }

  public void setPeCardio(String peCardio) {
    this.peCardio = peCardio;
  }

  public String getPeEnt() {
    return peEnt;
  }

  public void setPeEnt(String peEnt) {
    this.peEnt = peEnt;
  }

  public String getPeGastro() {
    return peGastro;
  }

  public void setPeGastro(String peGastro) {
    this.peGastro = peGastro;
  }

  public String getPeGeneral() {
    return peGeneral;
  }

  public void setPeGeneral(String peGeneral) {
    this.peGeneral = peGeneral;
  }

  public String getPeGenital() {
    return peGenital;
  }

  public void setPeGenital(String peGenital) {
    this.peGenital = peGenital;
  }

  public String getPeOther() {
    return peOther;
  }

  public void setPeOther(String peOther) {
    this.peOther = peOther;
  }

  public String getPeRespiratory() {
    return peRespiratory;
  }

  public void setPeRespiratory(String peRespiratory) {
    this.peRespiratory = peRespiratory;
  }

  public String getPeSkin() {
    return peSkin;
  }

  public void setPeSkin(String peSkin) {
    this.peSkin = peSkin;
  }

  public Boolean getPregBreastFeeding() {
    return pregBreastFeeding;
  }

  public void setPregBreastFeeding(Boolean pregBreastFeeding) {
    this.pregBreastFeeding = pregBreastFeeding;
  }

  public Boolean getPregContraceptive() {
    return pregContraceptive;
  }

  public void setPregContraceptive(Boolean pregContraceptive) {
    this.pregContraceptive = pregContraceptive;
  }

  public Boolean getPregCurrPreg() {
    return pregCurrPreg;
  }

  public void setPregCurrPreg(Boolean pregCurrPreg) {
    this.pregCurrPreg = pregCurrPreg;
  }

  public Integer getPregGestation() {
    return pregGestation;
  }

  public void setPregGestation(Integer pregGestation) {
    this.pregGestation = pregGestation;
  }

  public Date getPregLmp() {
    return pregLmp;
  }

  public void setPregLmp(Date pregLmp) {
    this.pregLmp = pregLmp;
  }

  public Integer getPregNumAbortion() {
    return pregNumAbortion;
  }

  public void setPregNumAbortion(Integer pregNumAbortion) {
    this.pregNumAbortion = pregNumAbortion;
  }

  public Integer getPregNumLiveBirth() {
    return pregNumLiveBirth;
  }

  public void setPregNumLiveBirth(Integer pregNumLiveBirth) {
    this.pregNumLiveBirth = pregNumLiveBirth;
  }

  public Integer getPregNumMiscarriage() {
    return pregNumMiscarriage;
  }

  public void setPregNumMiscarriage(Integer pregNumMiscarriage) {
    this.pregNumMiscarriage = pregNumMiscarriage;
  }

  public Integer getPregNumPreg() {
    return pregNumPreg;
  }

  public void setPregNumPreg(Integer pregNumPreg) {
    this.pregNumPreg = pregNumPreg;
  }

  public Integer getPregNumStillBirth() {
    return pregNumStillBirth;
  }

  public void setPregNumStillBirth(Integer pregNumStillBirth) {
    this.pregNumStillBirth = pregNumStillBirth;
  }

  public String getPregRemark() {
    return pregRemark;
  }

  public void setPregRemark(String pregRemark) {
    this.pregRemark = pregRemark;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRfAlertness() {
    return rfAlertness;
  }

  public void setRfAlertness(String rfAlertness) {
    this.rfAlertness = rfAlertness;
  }

  public String getRfBreathing() {
    return rfBreathing;
  }

  public void setRfBreathing(String rfBreathing) {
    this.rfBreathing = rfBreathing;
  }

  public String getRfCirculation() {
    return rfCirculation;
  }

  public void setRfCirculation(String rfCirculation) {
    this.rfCirculation = rfCirculation;
  }

  public String getRfDefg() {
    return rfDefg;
  }

  public void setRfDefg(String rfDefg) {
    this.rfDefg = rfDefg;
  }

  public String getRfDehydration() {
    return rfDehydration;
  }

  public void setRfDehydration(String rfDehydration) {
    this.rfDehydration = rfDehydration;
  }

  public String getRosGeneral() {
    return rosGeneral;
  }

  public void setRosGeneral(String rosGeneral) {
    this.rosGeneral = rosGeneral;
  }

  public String getRosRespi() {
    return rosRespi;
  }

  public void setRosRespi(String rosRespi) {
    this.rosRespi = rosRespi;
  }

  public String getRosCardio() {
    return rosCardio;
  }

  public void setRosCardio(String rosCardio) {
    this.rosCardio = rosCardio;
  }

  public String getRosGastro() {
    return rosGastro;
  }

  public void setRosGastro(String rosGastro) {
    this.rosGastro = rosGastro;
  }

  public String getRosGenital() {
    return rosGenital;
  }

  public void setRosGenital(String rosGenital) {
    this.rosGenital = rosGenital;
  }

  public String getRosEnt() {
    return rosEnt;
  }

  public void setRosEnt(String rosEnt) {
    this.rosEnt = rosEnt;
  }

  public String getRosSkin() {
    return rosSkin;
  }

  public void setRosSkin(String rosSkin) {
    this.rosSkin = rosSkin;
  }

  public String getRosLocomotor() {
    return rosLocomotor;
  }

  public void setRosLocomotor(String rosLocomotor) {
    this.rosLocomotor = rosLocomotor;
  }

  public String getRosNeruology() {
    return rosNeruology;
  }

  public void setRosNeruology(String rosNeruology) {
    this.rosNeruology = rosNeruology;
  }

  public String getRosOther() {
    return rosOther;
  }

  public void setRosOther(String rosOther) {
    this.rosOther = rosOther;
  }

  @Override
  public String toString() {
    return "Consultation{" +
        "id='" + id + '\'' +
        ", userId='" + userId + '\'' +
        ", visitId='" + visitId + '\'' +
        ", triageId='" + triageId + '\'' +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", peCardio='" + peCardio + '\'' +
        ", peEnt='" + peEnt + '\'' +
        ", peGastro='" + peGastro + '\'' +
        ", peGeneral='" + peGeneral + '\'' +
        ", peGenital='" + peGenital + '\'' +
        ", peOther='" + peOther + '\'' +
        ", peRespiratory='" + peRespiratory + '\'' +
        ", peSkin='" + peSkin + '\'' +
        ", pregBreastFeeding=" + pregBreastFeeding +
        ", pregContraceptive=" + pregContraceptive +
        ", pregCurrPreg=" + pregCurrPreg +
        ", pregGestation=" + pregGestation +
        ", pregLmp=" + pregLmp +
        ", pregNumAbortion=" + pregNumAbortion +
        ", pregNumLiveBirth=" + pregNumLiveBirth +
        ", pregNumMiscarriage=" + pregNumMiscarriage +
        ", pregNumPreg=" + pregNumPreg +
        ", pregNumStillBirth=" + pregNumStillBirth +
        ", pregRemark='" + pregRemark + '\'' +
        ", remark='" + remark + '\'' +
        ", rfAlertness='" + rfAlertness + '\'' +
        ", rfBreathing='" + rfBreathing + '\'' +
        ", rfCirculation='" + rfCirculation + '\'' +
        ", rfDefg='" + rfDefg + '\'' +
        ", rfDehydration='" + rfDehydration + '\'' +
        ", rosGeneral='" + rosGeneral + '\'' +
        ", rosRespi='" + rosRespi + '\'' +
        ", rosCardio='" + rosCardio + '\'' +
        ", rosGastro='" + rosGastro + '\'' +
        ", rosGenital='" + rosGenital + '\'' +
        ", rosEnt='" + rosEnt + '\'' +
        ", rosSkin='" + rosSkin + '\'' +
        ", rosLocomotor='" + rosLocomotor + '\'' +
        ", rosNeruology='" + rosNeruology + '\'' +
        ", rosOther='" + rosOther + '\'' +
        '}';
  }
}


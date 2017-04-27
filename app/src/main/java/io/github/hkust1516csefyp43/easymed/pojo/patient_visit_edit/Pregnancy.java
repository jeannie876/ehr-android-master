package io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by Louis on 8/4/16.
 */
public class Pregnancy implements Serializable {
  private static final long serialVersionUID = 1L;

  private Date lmdDate;
  private Boolean currPreg;
  private Integer Gestation;
  private Boolean breastFeeding;
  private Boolean contraceptiveUse;
  private Integer noOfPregnancy;
  private Integer noOfLiveBirth;
  private Integer noOfMiscarriage;
  private Integer noOfAbortion;
  private Integer noOfStillBirth;
  private String otherInformation;

  public Pregnancy() {
    //empty constructor
  }

  public Boolean getCurrPreg() {
    return currPreg;
  }

  public void setCurrPreg(Boolean currPreg) {
    this.currPreg = currPreg;
  }

  public Integer getGestation() {
    return Gestation;
  }

  public void setGestation(Integer gestation) {
    Gestation = gestation;
  }

  public Date getLmdDate() {
    return lmdDate;
  }

  public void setLmdDate(Date lmdDate) {
    this.lmdDate = lmdDate;
  }

  public Boolean getBreastFeeding() {
    return breastFeeding;
  }

  public void setBreastFeeding(Boolean breastFeeding) {
    this.breastFeeding = breastFeeding;
  }

  public Boolean getContraceptiveUse() {
    return contraceptiveUse;
  }

  public void setContraceptiveUse(Boolean contraceptiveUse) {
    this.contraceptiveUse = contraceptiveUse;
  }

  public Integer getNoOfPregnancy() {
    return noOfPregnancy;
  }

  public void setNoOfPregnancy(Integer noOfPregnancy) {
    this.noOfPregnancy = noOfPregnancy;
  }

  public Integer getNoOfLiveBirth() {
    return noOfLiveBirth;
  }

  public void setNoOfLiveBirth(Integer noOfLiveBirth) {
    this.noOfLiveBirth = noOfLiveBirth;
  }

  public Integer getNoOfMiscarriage() {
    return noOfMiscarriage;
  }

  public void setNoOfMiscarriage(Integer noOfMiscarriage) {
    this.noOfMiscarriage = noOfMiscarriage;
  }

  public Integer getNoOfAbortion() {
    return noOfAbortion;
  }

  public void setNoOfAbortion(Integer noOfAbortion) {
    this.noOfAbortion = noOfAbortion;
  }

  public Integer getNoOfStillBirth() {
    return noOfStillBirth;
  }

  public void setNoOfStillBirth(Integer noOfStillBirth) {
    this.noOfStillBirth = noOfStillBirth;
  }

  public String getOtherInformation() {
    return otherInformation;
  }

  public void setOtherInformation(String otherInformation) {
    this.otherInformation = otherInformation;
  }

}
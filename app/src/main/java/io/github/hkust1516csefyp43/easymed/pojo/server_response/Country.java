package io.github.hkust1516csefyp43.easymed.pojo.server_response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Louis on 23/3/16.
 */
public class Country {
  @SerializedName("country_id")
  private String id;
  @SerializedName("english_name")
  private String englishName;
  @SerializedName("native_name")
  private String nativeName;
  @SerializedName("emoji")
  private String emoji;
  @SerializedName("phone_country_code")
  private Integer countryCode;    // Integer not int because it can be null
  @SerializedName("phone_number_syntax")
  private String phoneSyntax;

  public Country(String id, String englishName, String nativeName, String emoji, Integer countryCode, String phoneSyntax) {
    this.id = id;
    this.englishName = englishName;
    this.nativeName = nativeName;
    this.emoji = emoji;
    this.countryCode = countryCode;
    this.phoneSyntax = phoneSyntax;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEnglishName() {
    return englishName;
  }

  public void setEnglishName(String englishName) {
    this.englishName = englishName;
  }

  public String getNativeName() {
    return nativeName;
  }

  public void setNativeName(String nativeName) {
    this.nativeName = nativeName;
  }

  public String getEmoji() {
    return emoji;
  }

  public void setEmoji(String emoji) {
    this.emoji = emoji;
  }

  public Integer getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(Integer countryCode) {
    this.countryCode = countryCode;
  }

  public String getPhoneSyntax() {
    return phoneSyntax;
  }

  public void setPhoneSyntax(String phoneSyntax) {
    this.phoneSyntax = phoneSyntax;
  }

  @Override
  public String toString() {
    return "Country{" + "id='" + id + '\'' + ", englishName='" + englishName + '\'' +
        ", nativeName='" + nativeName + '\'' + ", emoji='" + emoji + '\'' + ", countryCode="
        + countryCode + ", phoneSyntax='" + phoneSyntax + '\'' + '}';
  }
}
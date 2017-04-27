package io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Louis on 8/4/16.
 */
public class ListOfCards implements Serializable {
  private static final long serialVersionUID = 1L;

  private ArrayList<Card> cardArrayList;
  private ArrayList<Card> cardArrayList2;

  public ListOfCards() {
    //empty constructor
  }

  public ListOfCards(ArrayList<Card> cardArrayList) {
    this.cardArrayList = cardArrayList;
  }

  public ListOfCards(ArrayList<Card> cardArrayList, ArrayList<Card> cardArrayList2) {
    this.cardArrayList = cardArrayList;
    this.cardArrayList2 = cardArrayList2;
  }

  public ArrayList<Card> getCardArrayList() {
    return cardArrayList;
  }

  public void setCardArrayList(ArrayList<Card> cardArrayList) {
    this.cardArrayList = cardArrayList;
  }

  public ArrayList<Card> getCardArrayList2() {
    return cardArrayList2;
  }

  public void setCardArrayList2(ArrayList<Card> cardArrayList2) {
    this.cardArrayList2 = cardArrayList2;
  }

  @Override
  public String toString() {
    return "ListOfCards{" +
        "cardArrayList=" + cardArrayList +
        ", cardArrayList2=" + cardArrayList2 +
        '}';
  }
}

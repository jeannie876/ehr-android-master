package io.github.hkust1516csefyp43.easymed.pojo;

import java.io.Serializable;

/**
 * Created by Louis on 4/6/2016.
 */
public class Stock implements Serializable {
  String name;
  int number;

  public Stock() {
  }

  public Stock(String name, int number) {
    this.name = name;
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return getName();
  }
}

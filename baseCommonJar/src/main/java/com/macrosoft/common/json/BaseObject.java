package com.macrosoft.common.json;

public class BaseObject
{
  private String userName;
  private String userCode;
  private double weight;
  private int height;
  private boolean sex;
  private String[] array;
  private BaseObject innerObject;

  public String getUserName()
  {
    return this.userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getUserCode() {
    return this.userCode;
  }
  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }
  public double getWeight() {
    return this.weight;
  }
  public void setWeight(double weight) {
    this.weight = weight;
  }
  public int getHeight() {
    return this.height;
  }
  public void setHeight(int height) {
    this.height = height;
  }
  public boolean isSex() {
    return this.sex;
  }
  public void setSex(boolean sex) {
    this.sex = sex;
  }

  public BaseObject getInnerObject() {
    return this.innerObject;
  }
  public void setInnerObject(BaseObject innerObject) {
    this.innerObject = innerObject;
  }
  public String[] getArray() {
    return this.array;
  }
  public void setArray(String[] array) {
    this.array = array;
  }
}
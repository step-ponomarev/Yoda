package edu.ponomarev.step.component;

public enum BoxType {
  INBOX(0, "Inbox"),
  DAY(1, "Today"),
  WEEK(2, "Week"),
  LATE(3, "Late");

  private int value;
  private String name;

  BoxType(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public int toInteger() {
    return value;
  }

  @Override
  public String toString() { return name; }
}

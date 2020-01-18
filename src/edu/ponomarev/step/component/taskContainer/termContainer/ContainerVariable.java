package edu.ponomarev.step.component.taskContainer.termContainer;

public class ContainerVariable {
  public ContainerType type;
  public String name;
  final public static ContainerVariable[] BOX_VARIABLES = new ContainerVariable[] {
      new ContainerVariable(ContainerType.INBOX, "Inbox"),
      new ContainerVariable(ContainerType.DAY, "Today"),
      new ContainerVariable(ContainerType.WEEK, "Week"),
      new ContainerVariable(ContainerType.LATE, "Late")
  };

  public enum ContainerType {
    INBOX(0),
    DAY(1),
    WEEK(2),
    LATE(3);

    private int value;

    ContainerType(int i) {
      this.value = i;
    }

    public int toInteger() {
      return value;
    }
  }

  public static String getBoxName(ContainerType containerType) {
    for (var item : ContainerVariable.BOX_VARIABLES) {
      if (containerType.equals(item.type)) {
        return item.name;
      }
    }

    return null;
  }

  public ContainerVariable(ContainerType type, String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}

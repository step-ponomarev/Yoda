package edu.ponomarev.step.handler;

import edu.ponomarev.step.boxes.Box;
import edu.ponomarev.step.boxes.BoxType;
import edu.ponomarev.step.task.Task;
import edu.ponomarev.step.boxes.TaskStorage;

import java.util.Map;

public class TaskHandler {
  private Map<BoxType, Box> box;

  private BoxType currentBox;

  public TaskHandler() {
    box.put(BoxType.INBOX, new Box());
    box.put(BoxType.FREE, new Box());
    box.put(BoxType.COMPLETED, new Box());
  }

  public void complete(Task task) {
    task.noticeAll();
    box.get(BoxType.COMPLETED).add(task);
  }

  public void add(Task task) {
    box.get(currentBox).add(task);
  }

  public Box getBox() {
    return box.get(currentBox);
  }

  public void setType(BoxType type) {
    this.currentBox = type;
  }
}

//TODO: Задача может быть как в свободном списке, так и в проекте, но нахождение, но ее нахождение в инбоксе
// исключает 2 первых пункта. Так же нужно придумать как перемещать задачи.
package edu.ponomarev.step.data.offile;

import edu.ponomarev.step.data.DataWorker;
import edu.ponomarev.step.manager.TaskHandler;
import edu.ponomarev.step.task.Task;

import java.util.List;

public class Serializator implements DataWorker {

  public Serializator() {
    return;
  }

  @Override
  public void put(TaskHandler.BoxType type, Task task) throws Exception {

  }

  @Override
  public List pull(TaskHandler.BoxType type) throws Exception {
    return null;
  }
}

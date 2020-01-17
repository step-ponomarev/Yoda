package edu.ponomarev.step.MVC.view.taskPanel;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.TermTaskContainer;
import edu.ponomarev.step.MVC.model.TaskWorker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskPanel extends JPanel {
  private TermTaskContainer.ContainerType selectedType;
  private HashMap<TermTaskContainer.ContainerType, JList> listMap;
  private ArrayList<TaskBoxModule> boxModules;

  public TaskPanel() {
    super();
    this.listMap = new HashMap<>();
    this.boxModules = new ArrayList<>();
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    //Creating TaskBlocks in certain sequence
    for (TermTaskContainer.ContainerVariable boxVariavle : TermTaskContainer.BOX_VARIABLES) {
      final var boxContainerType = boxVariavle.type;

      this.listMap.get(boxContainerType).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      this.boxModules.add(new TaskBoxModule(TaskWorker.getBoxName(boxContainerType),
          this.listMap.get(boxContainerType), boxContainerType));
    }

    for (TaskBoxModule boxModule : boxModules) {
      this.add(boxModule);
      boxModule.run();
    }

    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }


  public HashMap<TermTaskContainer.ContainerType, JList> getListMap() {
    return listMap;
  }

  public void setListMap(HashMap<TermTaskContainer.ContainerType, TermTaskContainer> listMap) {
    this.listMap.clear();
    for (TermTaskContainer.ContainerVariable boxVariavle : TermTaskContainer.BOX_VARIABLES) {
      this.listMap.put(boxVariavle.type, new JList(listMap.get(boxVariavle.type).toArray()));
    }
  }

  public void repaintAllModules() {
    for (Map.Entry<TermTaskContainer.ContainerType, JList> boxBlock : listMap.entrySet()) {
      boxBlock.getValue().repaint();
    }
  }

  public Task getSelectedTask() {
    Task task = null;
    for (Map.Entry<TermTaskContainer.ContainerType, JList> boxBlock : listMap.entrySet()) {
      if (!boxBlock.getValue().isSelectionEmpty()) {
        task = (Task) boxBlock.getValue().getSelectedValue();
        break;
      }
    }

    return task;
  }

  public TermTaskContainer.ContainerType getSelectedType() {
    return selectedType;
  }

  public void setSelectedType(TermTaskContainer.ContainerType selectedType) {
    this.selectedType = selectedType;
  }

  public ArrayList<TaskBoxModule> getBoxModules() {
    return boxModules;
  }

  public void setBoxModules(ArrayList<TaskBoxModule> boxModules) {
    this.boxModules = boxModules;
  }
}

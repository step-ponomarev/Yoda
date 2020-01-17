package edu.ponomarev.step.MVC.view.taskPanel;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable;
import edu.ponomarev.step.component.taskContainer.termContainer.TermTaskContainer;
import edu.ponomarev.step.component.taskContainer.termContainer.ContainerVariable.ContainerType;
import edu.ponomarev.step.MVC.model.TaskWorker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskPanel extends JPanel {
  private ContainerType selectedType;
  private HashMap<ContainerType, JList> listMap;
  private ArrayList<TaskBoxModule> boxModules;

  public TaskPanel() {
    super();
    this.listMap = new HashMap<>();
    this.boxModules = new ArrayList<>();
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    //Creating TaskBlocks in certain sequence
    for (ContainerVariable boxVariavle : ContainerVariable.BOX_VARIABLES) {
      final var boxContainerType = boxVariavle.type;

      this.listMap.get(boxContainerType).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      this.boxModules.add(new TaskBoxModule(ContainerVariable.getBoxName(boxContainerType),
          this.listMap.get(boxContainerType), boxContainerType));
    }

    for (TaskBoxModule boxModule : boxModules) {
      this.add(boxModule);
      boxModule.run();
    }

    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }


  public HashMap<ContainerType, JList> getListMap() {
    return listMap;
  }

  public void setListMap(HashMap<ContainerType, TermTaskContainer> listMap) {
    this.listMap.clear();
    for (ContainerVariable boxVariavle : ContainerVariable.BOX_VARIABLES) {
      this.listMap.put(boxVariavle.type, new JList(listMap.get(boxVariavle.type).toArray()));
    }
  }

  public void repaintAllModules() {
    for (Map.Entry<ContainerType, JList> boxBlock : listMap.entrySet()) {
      boxBlock.getValue().repaint();
    }
  }

  public Task getSelectedTask() {
    Task task = null;
    for (Map.Entry<ContainerType, JList> boxBlock : listMap.entrySet()) {
      if (!boxBlock.getValue().isSelectionEmpty()) {
        task = (Task) boxBlock.getValue().getSelectedValue();
        break;
      }
    }

    return task;
  }

  public ContainerType getSelectedType() {
    return selectedType;
  }

  public void setSelectedType(ContainerType selectedType) {
    this.selectedType = selectedType;
  }

  public ArrayList<TaskBoxModule> getBoxModules() {
    return boxModules;
  }

  public void setBoxModules(ArrayList<TaskBoxModule> boxModules) {
    this.boxModules = boxModules;
  }
}

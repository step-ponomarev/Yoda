package edu.ponomarev.step.MVC.view.taskPanel;

import edu.ponomarev.step.component.task.TaskContainer;
import edu.ponomarev.step.MVC.model.DataHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskPanel extends JPanel {
  private JScrollPane scrollPane;
  private JLabel boxLabel;

  private HashMap<DataHandler.BoxType, JList> listMap;

  private ArrayList<TaskBoxModule> boxModules;

  private DataHandler.BoxType currentBoxType;

  public TaskPanel() {
    super();
    this.boxLabel = new JLabel();
    this.listMap = new HashMap<DataHandler.BoxType, JList>();
    this.boxModules = new ArrayList<TaskBoxModule>();
  }

  public void run() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    //Creating TaskBlocks in certain sequence
    for (DataHandler.BoxVariable boxVariavle : DataHandler.BOX_VARIABLES) {
      DataHandler.BoxType boxType = boxVariavle.type;
      listMap.get(boxType).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      this.boxModules.add(new TaskBoxModule(DataHandler.getBoxName(boxType), listMap.get(boxType), boxType));
    }

    boxLabel.setBackground(Color.WHITE);

    for (TaskBoxModule boxModule : boxModules) {
      this.add(boxModule);
      boxModule.run();
    }

    this.setBackground(Color.WHITE);
    this.setVisible(true);
  }

  public DataHandler.BoxType getCurrentBoxType() {
    return currentBoxType;
  }

  public void setCurrentBoxType(DataHandler.BoxType currentBoxType) {
    this.currentBoxType = currentBoxType;
  }

  public HashMap<DataHandler.BoxType, JList> getListMap() {
    return listMap;
  }

  public void setListMap(HashMap<DataHandler.BoxType, TaskContainer> listMap) {
    this.listMap.clear();
    for (DataHandler.BoxVariable boxVariavle : DataHandler.BOX_VARIABLES) {
      this.listMap.put(boxVariavle.type, new JList(listMap.get(boxVariavle.type).toArray()));
    }
  }

  public void repaintAllModules() {
    for (Map.Entry<DataHandler.BoxType, JList> boxBlock : listMap.entrySet()) {
      boxBlock.getValue().repaint();
    }
  }

  public JScrollPane getScrollPane() {
    return scrollPane;
  }

  public void setScrollPane(JScrollPane scrollPane) {
    this.scrollPane = scrollPane;
  }

  public ArrayList<TaskBoxModule> getBoxModules() {
    return boxModules;
  }

  public void setBoxModules(ArrayList<TaskBoxModule> boxModules) {
    this.boxModules = boxModules;
  }

  public void setBoxLabel(String label) {
    boxLabel.setText(label);
  }

  public JLabel getBoxLabel() {
    return boxLabel;
  }
}

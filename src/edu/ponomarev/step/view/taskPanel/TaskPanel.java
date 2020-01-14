package edu.ponomarev.step.view.taskPanel;

import edu.ponomarev.step.component.task.Task;
import edu.ponomarev.step.component.task.TaskContainer;
import edu.ponomarev.step.manager.DataHandler;

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

    for (Map.Entry<DataHandler.BoxType, JList> boxList : listMap.entrySet()) {
      DataHandler.BoxType boxType = boxList.getKey();
      boxList.getValue().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      this.boxModules.add(new TaskBoxModule(DataHandler.getBoxName(boxType), boxList.getValue(), boxType));
    }

    boxLabel.setBackground(Color.WHITE);

    for (TaskBoxModule boxModule : boxModules) {
      this.add(boxModule);
      boxModule.run();
    }

    this.setBackground(new Color(255, 255, 255));
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

    for (Map.Entry<DataHandler.BoxType, TaskContainer> taksBox : listMap.entrySet()) {
      this.listMap.put(taksBox.getKey(), new JList(taksBox.getValue().getList().toArray()));
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

package edu.ponomarev.step.manager;

import java.sql.*;

public class DBWorker implements Runnable {
    private TaskHandler currentHandlerk;

    public DBWorker(TaskHandler handler) {
        this.currentHandlerk = handler;
    }

    @Override
    public void run() {

    }
}

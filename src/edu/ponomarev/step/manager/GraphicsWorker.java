package edu.ponomarev.step.manager;

import edu.ponomarev.step.graphics.Main.Window;

public class GraphicsWorker implements Runnable {
    private Window window;

    public GraphicsWorker(Window window) {
      this.window = window;
    }

    @Override
    public void run() {
        window.run();
    }
}

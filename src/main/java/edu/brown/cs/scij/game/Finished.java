package edu.brown.cs.scij.game;

public class Finished {
  private boolean finished;

  public Finished(boolean finished) {
    this.finished = finished;
  }

  public void setFinished(boolean b) {
    finished = b;
  }

  public boolean isFinished() {
    return finished;
  }

}

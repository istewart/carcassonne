package edu.brown.cs.scij.tile;

import edu.brown.cs.scij.game.Meeple;

public class Edge {
  private final EDGEFEATURE feature;
  private final boolean isMeepleable;
  private Meeple meeple;

  public Edge(EDGEFEATURE feature, boolean isMeepleable) {
    this.feature = feature;
    this.isMeepleable = isMeepleable;
    this.meeple = null;
  }

  public Meeple getMeeple() {
    return meeple;
  }

  public boolean getIsMeeplable() {
    return isMeepleable;
  }

  public EDGEFEATURE getFeature() {
    return feature;
  }

  public void setMeeple(Meeple m) {
    this.meeple = m;
  }
}

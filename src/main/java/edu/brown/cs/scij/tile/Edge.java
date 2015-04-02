package edu.brown.cs.scij.tile;

import edu.brown.cs.scij.game.Meeple;

public class Edge {
  private final EdgeFeature feature;
  private final boolean isMeepleable;
  private Meeple meeple;

  public Edge(EdgeFeature feature, boolean isMeepleable) {
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

  public EdgeFeature getFeature() {
    return feature;
  }

  public void setMeeple(Meeple m) {
    this.meeple = m;
  }
}

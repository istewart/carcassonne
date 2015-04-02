package edu.brown.cs.scij.tile;

import edu.brown.cs.scij.game.Meeple;

abstract class Feature {
  private final Feature feature;
  private final boolean isMeepleable;
  private Meeple meeple;

  public Feature(Feature feature, boolean isMeepleable) {
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

  public Feature getFeature() {
    return feature;
  }

  public void setMeeple(Meeple m) {
    this.meeple = m;
  }
}

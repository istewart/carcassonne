package edu.brown.cs.scij.tile;

import edu.brown.cs.scij.game.Meeple;

public class Center {
  private final CenterFeature feature;
  private final boolean isMeepleable;
  private Meeple meeple;

  public Center(CenterFeature feature, boolean isMeepleable) {
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

  public CenterFeature getFeature() {
    return feature;
  }

  public void setMeeple(Meeple m) {
    this.meeple = m;
  }
}

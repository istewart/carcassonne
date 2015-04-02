package edu.brown.cs.scij.tile;

import edu.brown.cs.scij.game.Meeple;

abstract class TileFeature {
  protected final Feature feature;
  protected boolean isMeeplable;
  protected Meeple meeple;

  public TileFeature(Feature feature) {
    this.feature = feature;
    setIsMeeplable(feature);
    this.meeple = null;
  }

  public Meeple getMeeple() {
    return meeple;
  }

  public boolean getIsMeeplable() {
    return isMeeplable;
  }

  public Feature getFeature() {
    return feature;
  }

  public void setMeeple(Meeple m) {
    this.meeple = m;
  }

  private void setIsMeeplable(Feature feature) {
    if (feature == Feature.ENDPOINT || feature == Feature.RIVER) {
      isMeeplable = false;
    } else {
      isMeeplable = true;
    }
  }
}

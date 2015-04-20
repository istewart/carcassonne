package edu.brown.cs.scij.tile;

import edu.brown.cs.scij.game.Meeple;

public abstract class TileFeature {
  protected final Feature feature;
  protected boolean isMeeplable;
  protected Meeple meeple;

  public TileFeature(Feature feature) {
    this.feature = feature;
    setIsMeeplable(feature);
    this.meeple = null;
  }

  public boolean hasMeeple() {
    if (meeple == null) {
      return false;
    }
    return true;
  }

  public Meeple getMeeple() {
    return meeple;
  }

  public boolean isMeeplable() {
    return isMeeplable;
  }

  public Feature getFeature() {
    return feature;
  }

  public void setMeeple(Meeple m) {
    this.meeple = m;
  }

  public void removeMeeple() throws NullMeepleException {
    if (meeple == null) {
      throw new NullMeepleException("there is no meeple here to return");
    }
    meeple.getPlayer().returnMeeple();
    meeple = null;
  }

  private void setIsMeeplable(Feature feature) {
    if (feature == Feature.ENDPOINT || feature == Feature.RIVER
        || feature == Feature.FIELD) {
      isMeeplable = false;
    } else {
      isMeeplable = true;
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    // sb.append("Feature: ");
    switch (feature) {
      case CITY:
        sb.append("CITY");
        break;
      case ENDPOINT:
        sb.append("ENDPOINT");
        break;
      case FIELD:
        sb.append("FIELD");
        break;
      case MONASTERY:
        sb.append("MONASTERY");
        break;
      case RIVER:
        sb.append("RIVER");
        break;
      case ROAD:
        sb.append("ROAD");
        break;
      default:
        sb.append("SHOULDNT GET HERE NOOOO");
        break;
    }
    sb.append(" meeplable? " + isMeeplable);
    sb.append(" meeple: " + String.valueOf(meeple));
    return sb.toString();
  }
}

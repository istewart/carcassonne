package edu.brown.cs.scij.tile;

import edu.brown.cs.scij.game.Meeple;

/**
 * a feature that resides on a tile.
 * @author scij
 *
 */
public abstract class TileFeature {
  protected final Feature feature;
  protected boolean isMeeplable;
  protected Meeple meeple;
  protected boolean touchesMeeple;

  /**
   * constructor for TileFeature.
   * @param feature what feature is contained
   */
  public TileFeature(Feature feature) {
    this.feature = feature;
    setIsMeeplable(feature);
    this.meeple = null;
    this.touchesMeeple = false;
  }

  /**
   * determines whether the feature has a meeple.
   * @return whether the feature has a meeple
   */
  public boolean hasMeeple() {
    if (meeple == null) {
      return false;
    }
    return true;
  }

  /**
   * getter for meeple.
   * @return meeple
   */
  public Meeple getMeeple() {
    return meeple;
  }

  /**
   * determines whether the feature is meeplable.
   * @return isMeeplable
   */
  public boolean isMeeplable() {
    return isMeeplable;
  }

  /**
   * getter for feature.
   * @return feature
   */
  public Feature getFeature() {
    return feature;
  }
  
  /**
   * determines whether the feature touches a meeple.
   * @return touchesMeeple
   */
  public boolean touchesMeeple() {
	  return touchesMeeple;
  }

  /**
   * setter for touchesMeeple
   * @param tm what to set touchesMeeple to
   */
  public void setTouchesMeeple(boolean tm){
	  touchesMeeple = tm;
  }

  /**
   * setter for meeple.
   * @param m what to set meeple to
   * @throws UnMeeplableException if you cant meeple this feature
   */
  public void setMeeple(Meeple m) throws UnMeeplableException {
    if (isMeeplable) {
      this.meeple = m;
      touchesMeeple = true;
    } else {
      throw new UnMeeplableException("This feature cannot be meepled");
    }
  }

  /**
   * gets rid of the meeple.
   * @throws NullMeepleException when there is no meeple to remove
   */
  public void removeMeeple() throws NullMeepleException {
    if (meeple == null) {
      throw new NullMeepleException("there is no meeple here to return");
    }
    meeple.getPlayer().returnMeeple();
    touchesMeeple = false;
    meeple = null;
  }

  /**
   * setter for isMeeplable.
   * @param feature what feature the meeple would be on.
   */
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
    sb.append(" meeplable: " + isMeeplable);
    sb.append(", Belongs to: " + String.valueOf(meeple));
    sb.append(", touchesMeeple: " + touchesMeeple);
    return sb.toString();
  }
}

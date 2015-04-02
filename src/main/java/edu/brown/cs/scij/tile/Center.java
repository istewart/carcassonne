package edu.brown.cs.scij.tile;

/**
 * Center is an object that keeps track of the center element of a tile. It
 * contains an Enum describing what type of center is part of the tile, and the
 * meeple that is on it, if there is one.
 * 
 * @author scij
 *
 */
public class Center extends TileFeature {

  public Center(Feature feature) {
    super(feature);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    int fromFeature = 0;
    int fromMeeple = 0;
    if (feature != null) {
      fromFeature = feature.hashCode();
    }
    if (meeple != null) {
      fromMeeple = meeple.hashCode();
    }

    result = prime * result + fromFeature;
    result = prime * result + fromMeeple;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Center)) {
      return false;
    }
    Center other = (Center) obj;
    if (feature != other.feature) {
      return false;
    }
    if (meeple == null) {
      if (other.meeple != null) {
        return false;
      }
    } else if (!meeple.equals(other.meeple)) {
      return false;
    }
    return true;
  }

}

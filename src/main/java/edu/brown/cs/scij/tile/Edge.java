package edu.brown.cs.scij.tile;

public class Edge extends TileFeature {

  public Edge(Feature feature) {
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
    if (!(obj instanceof Edge)) {
      return false;
    }
    Edge other = (Edge) obj;
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

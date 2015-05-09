package edu.brown.cs.scij.tile;

/**
 * Edge is an object that keeps track of an edge element of a tile. It
 * contains an Enum describing what type of edge is part of the tile, and the
 * meeple that is on it, if there is one.
 *
 * @author scij
 *
 */
public class Edge extends TileFeature {

  /**
   * constructor for Edge.
   * @param feature the feature contained by edge.
   * @throws InvalidEdgeException if this is not an acceptable feature
   */
  public Edge(Feature feature)
    throws InvalidEdgeException {
    super(feature);
    if (feature == Feature.MONASTERY || feature == Feature.ENDPOINT) {
      throw new InvalidEdgeException(
          "monestaries and endpoints cant be on edge.");
    }
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

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

  public Center(Feature feature, boolean isMeepleable) {
    super(feature, isMeepleable);
  }
}

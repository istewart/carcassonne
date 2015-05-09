package edu.brown.cs.scij.network;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A <code>Key</code> provides access to the Server.
 * <p>
 * Each <code>Key</code> has a unique id that is different from all other
 * <code>Key</code>s in its generation. When created through the
 * <code>static</code> {@link #generate() generate} method all <code>Key
 * </code>s will have a positive id. This allows a user to reserve the id 0 and
 * any negative number for notable keys or primary keys whose ids need to be
 * consistent.
 */
public final class Key {
  private static Set<Integer> existing = new HashSet<>();
  private static int activeGeneration = 0;

  private final int id;
  private final int generation;

  /**
   * Creates a new Key with the given id. This does not check if the key
   * already exists.
   * @param id The id of the created key.
   */
  private Key(int id) {
    existing.add(id);
    this.id = id;
    this.generation = activeGeneration;
  }

  private Key(int generation, int id) {
    this.id = id;
    this.generation = generation;
    if (generation == activeGeneration) {
      existing.add(id);
    }
  }

  /**
   * Sets the generation of Key that will be accepted in this program.
   * @param generation The generation of Key to make active.
   */
  public static void setActiveGeneration(int generation) {
    activeGeneration = generation;
  }

  /**
   * Returns the id of this Key. Throws a RuntimeException if <code>this</code>
   * is deprecated.
   * @return The id of this Key
   * @throws DeprecatedKeyException if this is called on a deprecated Key.
   */
  public int getId() throws DeprecatedKeyException {
    if (isActive()) {
      return id;
    } else {
      throw new DeprecatedKeyException();
    }
  }

  /**
   * Factory method that creates a Key with this id.
   * @param id The id of the Key to create
   * @return The new Key or <code>null</code> if the Key already exists
   */
  public static Key getKey(int id) {
    assert id != -1;
    if (existing.contains(id)) {
      return null;
    } else {
      return new Key(id);
    }
  }

  /**
   * Returns a copy of a Key with the given id, whether one already exists or
   * not. If the Key does not already exist, it is added to the cache.
   * @param id The id of the Key to copy
   * @return A copy of the Key with the given id
   */
  public static Key getCopy(int id) {
    return new Key(id);
  }

  /**
   * Returns a copy of the given Key.
   * @param k The Key to copy
   * @return A copy of the Key with the given id
   */
  public static Key getCopy(Key k) {
    return k;
  }

  /**
   * Generates a new, unique Key with an id that is 1 or greater.
   * <p>
   * These ids are generated randomly, but guaranteed not to collide with other
   * Keys in memory.
   * @return The new Key
   */
  public static Key generate() {
    int i;
    do {
      i = generateId();
    } while (existing.contains(i));
    assert i != 0;
    existing.add(i);
    return new Key(i);
  }

  /**
   * The Key 0 is never generated. This key can be used as a master key, since
   * it is always available.
   */
  private static int generateId() {
    return new Random().nextInt(Integer.MAX_VALUE) + 1;
  }

  /**
   * Clears all cached Keys and depredates existing keys, rendering them
   * unusable.
   */
  public static void deprecate() {
    activeGeneration++;
    existing = new HashSet<>();
  }

  /**
   * Tells whether this Key is active or not.
   * @return <code>true</code> if this Key is active, and <code>false</code> if
   *         it is not.
   */
  public boolean isActive() {
    return generation == activeGeneration;
  }

  /**
   * Returns a String representation of this Key that can be transferred over
   * by a JSON request.
   * @return The String representation
   */
  public String toJSONString() {
    return Integer.toString(generation) + "." + Integer.toString(id);
  }

  /**
   * Recreates a Key given the return value of Key.toJSONString().
   * @param s The String to convert
   * @return The equivalent Key
   */
  public static Key fromJSONString(String s) {
    if (s.equals("null")) {
      return null;
    }
    String[] ss = s.split("\\.");
    return new Key(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]));
  }

  @Override
  public String toString() {
    return (isActive() ? Integer.toString(id) : "(Deprecated)");
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Key) {
      Key k = (Key) o;
      if (isActive() && k.isActive()) {
        try {
          return id == k.getId();
        } catch (DeprecatedKeyException ex) {
          return false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    if (isActive()) {
      return id;
    } else {
      return -1;
    }
  }
}

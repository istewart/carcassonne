package edu.brown.cs.jbellavi.network;

import java.util.Map;

/**
 * Controls front-end access to the back end/game engine. Also contains some
 * mediation methods that allow for updating and connecting to the game.
 */
interface Server {
  /**
   * Pings the server.
   * @param player The {@link Key} of the player who is pinging
   * @return <code>true</code> if the player needs to update,
   * <code>false</code> if the player's information is up to date
   */
  Boolean ping(Key player);

  /**
   * Gets the number of times a player has pinged the server.
   * @param player The {@link Key} of the player who is pinging
   * @return The number of times <code>player</code> has pinged the server
   */
  int pingCount(Key player);

  /**
   * Connects the player with the given ip to the server.
   * @param ip The ip of the player connecting
   * @return The {@link Key} the player must use to connect
   */
  Map<String, Object> connect(String ip);

  /**
   * Gets all necessary updates.
   * @param player The {@link Key} for player sending the request
   * @return A <code>Map&lt;String, Object&gt;</code> containing a mapping
   * between names of fields and the values of those fields. This mapping
   * only contains the fields that have not changed since the last time
   * update() was called for this player.
   */
  Map<String, Object> update(Key player);

  /**
   * Gets a Map containing all of the fields stored by the server.
   * @return A <code>Map&lt;String, Object&gt;</code> containing all fields
   * stored by the server
   */
  Map<String, Object> getAllFields();

  /**
   * Changes the value of a field and notifies all clients of this change.
   * @param field The field to change
   * @param value The value to set this field to
   */
  void putField(String field, Object value);

  /**
   * Closes all resources associated with this server. Must be called before
   * the server is freed or it may continue to consume resources.
   */
  void close();

  /**
   * Gets the value of a specific field.
   * @param field The field to get
   * @return The value of that field
   */
  Object getField(String field);

  /**
   * Marks that a player has closed the game window.
   * @param k The Key of the player to disconect.
   */
  void disconnect(Key k);

  /**
   * Prevents any new users from connecting to the server. Allows old players
   * to continue to reconnect.
   */
  // void seal();

  /* 
   * TODO: Allow (force) users to do a full data grab when they first connect
   * or force them to. 
   */
}

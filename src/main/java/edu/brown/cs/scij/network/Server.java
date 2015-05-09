package edu.brown.cs.scij.network;

import java.util.Map;

/**
 * Controls front-end access to the back end/game engine. Also contains some
 * mediation methods that allow for updating and connecting to the game.
 */
public interface Server {
  /**
   * Pings the server.
   * @param player The {@link Key} of the player who is pinging
   * @return <code>true</code> if the player needs to update, <code>false</code>
   *         if the player's information is up to date
   * @throws NoSuchPlayerException when a player tries to ping with an invalid
   *         Key.
   */
  boolean ping(Key player) throws NoSuchPlayerException;

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
   *         between names of fields and the values of those fields. This
   *         mapping
   *         only contains the fields that have not changed since the last time
   *         update() was called for this player.
   */
  Map<String, Object> update(Key player);

  /**
   * Gets all fields and marks everything updated for a single player. This
   * includes fields the player has already gotten or fields that were last
   * updated before this player joined. This can be used upon page load, etc.
   * when previous data has been lost by the front end.
   * @param player The {@link Key} of the player sending hte requst
   * @return a <code>Map&lt;String, Object&gt;</code> containing a mapping
   *         between names of fields and the values of those fields. It contains
   *         all
   *         fields stored in the Server.
   */
  Map<String, Object> updateAll(Key player);

  /**
   * Gets a Map containing all of the fields stored by the server.
   * @return A <code>Map&lt;String, Object&gt;</code> containing all fields
   *         stored by the server
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
   * Prevents any new ip addresses from connecting to the server. Allows
   * people already in the game to continue to connect, disconnect, and
   * reconnect.
   */
  void seal();

  /**
   * Removes a seal that has been placed on the server.
   */
  void unseal();

  /**
   * Sets the Server's {@link BackEnd}.
   * @param back The BackEnd to use
   * @return <code>this</code>
   */
  Server setBackEnd(BackEnd back);

  /**
   * Forwards the ask request to the BackEnd and returns the object that
   * the back end returns.
   * @param key The key of the player asking the question
   * @param field The field the query should be applied to
   * @param value The value of the field
   * @return A response object or <code>null</code> if the query could
   *         not be processed by the BackEnd or no BackEnd exists.
   */
  Object ask(Key key, String field, Map<String, String> value);
}
